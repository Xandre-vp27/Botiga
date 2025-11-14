/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.util.Connexio;
import com.mycompany.botiga.model.Comanda;
import com.mycompany.botiga.model.Descompte;
import com.mycompany.botiga.model.LiniaComanda;
import com.mycompany.botiga.model.Producte;

import java.sql.*;
import java.util.List;

public class ComandaDAO {

    public void crearComanda(Comanda comanda, List<LiniaComanda> linies) throws SQLException {
        Connection conn = null;

        try {
            conn = Connexio.getConnection();
            conn.setAutoCommit(false);

            // 1. INSERIR LA COMANDA A LA TAULA COMANDES
            String sqlComanda = "INSERT INTO Comandes (client_id, total) VALUES (?, 0)";
            try (PreparedStatement psComanda = conn.prepareStatement(sqlComanda, Statement.RETURN_GENERATED_KEYS)) {
                psComanda.setInt(1, comanda.getClient_id());

                psComanda.executeUpdate();

                // Obtenir l'ID generat
                try (ResultSet rs = psComanda.getGeneratedKeys()) {
                    if (rs.next()) {
                        int comandaId = rs.getInt(1);
                        comanda.setId(comandaId); // Assignem l'ID al model

                        // 2. INSERIR LES LÍNIES DE COMANDA I ACTUALITZAR ESTOC
                        // Reutilitzem la connexió 'conn' i l'ID de la comanda
                        for (LiniaComanda linia : linies) {
                            // 2.1. ACTUALITZAR L'ESTOC DEL PRODUCTE
                            // Preu per l'actualització: comprovar si hi ha prou estoc
                            String sqlEstoc = "UPDATE Productes SET estoc = estoc - ? WHERE id = ? AND estoc >= ?";
                            try (PreparedStatement psEstoc = conn.prepareStatement(sqlEstoc)) {
                                psEstoc.setInt(1, linia.getQuantitat());
                                psEstoc.setInt(2, linia.getProducte_id());
                                psEstoc.setInt(3, linia.getQuantitat()); // Condició: estoc actual >= quantitat

                                int filesAfectades = psEstoc.executeUpdate();

                                if (filesAfectades == 0) {
                                    // Si no s'ha afectat cap fila, vol dir que l'estoc era insuficient.
                                    // Això causarà que es salti al 'catch' i es faci 'rollback'.
                                    throw new SQLException("Estoc insuficient per al Producte ID: " + linia.getProducte_id());
                                }
                            }

                            // 2.2. INSERIR LA LÍNIA DE COMANDA
                            String sqlLinia = "INSERT INTO LiniesComanda (comanda_id, producte_id, quantitat, preuUnitari) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement psLinia = conn.prepareStatement(sqlLinia)) {
                                psLinia.setInt(1, comanda.getId()); // Usar l'ID obtingut
                                psLinia.setInt(2, linia.getProducte_id());
                                psLinia.setInt(3, linia.getQuantitat());
                                psLinia.setDouble(4, linia.getPreuUnitari()); // Aquest preu ha de venir del model/DAO

                                psLinia.executeUpdate();
                            }
                        }

                        // 3. ESTABLIR SAVEPOINT ABANS D'APLICAR DESCOMPTES
                        Savepoint spDescomptes = null;
                        try {
                            spDescomptes = conn.setSavepoint("DespresLinies");

                            // 3.1. Crida al mètode privat per aplicar la lògica de descompte
                            aplicarDescomptes(conn, comanda.getId());

                        } catch (SQLException e) {
                            // Si la lògica d'aplicació de descomptes (dins del mètode privat) llença un error:
                            if (spDescomptes != null) {
                                conn.rollback(spDescomptes); // Tornem a l'estat just després d'inserir les línies 
                            }
                            // Mostrem l'avís i continuem amb el COMMIT de la comanda sense descomptes aplicats
                            System.err.println("AVÍS: No s'han pogut aplicar els descomptes. Es continua sense ells. Causa: " + e.getMessage());
                        }

                    } else {
                        throw new SQLException("Error en crear la comanda, no s'ha obtingut l'ID.");
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Aplica els descomptes als productes de les línies de comanda. Nota:
     * Assumeix que ja estem dins d'una transacció amb autoCommit=false.
     *
     * @param conn Connexió activa (dins de la transacció)
     * @param comandaId ID de la comanda en procés
     * @throws SQLException Si un error impedeix aplicar el descompte, es llança
     * i el codi cridador ha de fer rollback al savepoint.
     */
    private void aplicarDescomptes(Connection conn, int comandaId) throws SQLException {
        // 1. OBTENIR DESCOMPTES DISPONIBLES
        // En un projecte real, cridaríem a DescompteDAO.llistarPerComanda(comandaId)
        // Per a la pràctica, farem una consulta directa.

        String sqlDescomptes = "SELECT d.tipus, d.valor, lc.producte_id, lc.id AS linia_id, lc.preuUnitari "
                + "FROM LiniesComanda lc "
                + "JOIN Descomptes d ON lc.producte_id = d.producte_id "
                + "WHERE lc.comanda_id = ?";

        // 2. ITERAR I APLICAR A CADA LÍNIA
        try (PreparedStatement psDescomptes = conn.prepareStatement(sqlDescomptes)) {
            psDescomptes.setInt(1, comandaId);

            try (ResultSet rs = psDescomptes.executeQuery()) {
                while (rs.next()) {
                    int liniaId = rs.getInt("linia_id");
                    String tipus = rs.getString("tipus");
                    double valorDescompte = rs.getDouble("valor");
                    double preuActual = rs.getDouble("preuUnitari");
                    double nouPreu = preuActual;

                    // Càlcul del nou preu
                    if (tipus.equals("%")) {
                        nouPreu = preuActual * (1 - (valorDescompte / 100.0));
                    } else if (tipus.equals("€")) {
                        nouPreu = preuActual - valorDescompte;
                        if (nouPreu < 0) {
                            nouPreu = 0; // Evitar preus negatius
                        }
                    }

                    // 3. ACTUALITZAR EL PREU DE LA LÍNIA DE COMANDA
                    String sqlUpdateLinia = "UPDATE LiniesComanda SET preuUnitari = ? WHERE id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateLinia)) {
                        psUpdate.setDouble(1, nouPreu);
                        psUpdate.setInt(2, liniaId);
                        psUpdate.executeUpdate();
                    }
                }
            }
        }
    }
}
