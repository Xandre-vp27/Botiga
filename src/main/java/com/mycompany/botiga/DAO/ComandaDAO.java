/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.util.Connexio;
import com.mycompany.botiga.model.Comanda;
import com.mycompany.botiga.model.LiniaComanda;

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
                        for (LiniaComanda linia : linies) {
                            // 2.1. ACTUALITZAR L'ESTOC DEL PRODUCTE
                            String sqlEstoc = "UPDATE Productes SET estoc = estoc - ? WHERE id = ? AND estoc >= ?";
                            try (PreparedStatement psEstoc = conn.prepareStatement(sqlEstoc)) {
                                psEstoc.setInt(1, linia.getQuantitat());
                                psEstoc.setInt(2, linia.getProducte_id());
                                psEstoc.setInt(3, linia.getQuantitat());

                                int filesAfectades = psEstoc.executeUpdate();
                                if (filesAfectades == 0) {
                                    throw new SQLException("Estoc insuficient per al Producte ID: " + linia.getProducte_id());
                                }
                            }

                            // 2.2. INSERIR LA LÍNIA DE COMANDA
                            String sqlLinia = "INSERT INTO LiniesComanda (comanda_id, producte_id, quantitat, preuUnitari) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement psLinia = conn.prepareStatement(sqlLinia)) {
                                psLinia.setInt(1, comanda.getId());
                                psLinia.setInt(2, linia.getProducte_id());
                                psLinia.setInt(3, linia.getQuantitat());
                                psLinia.setDouble(4, linia.getPreuUnitari());
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
                            if (spDescomptes != null) {
                                conn.rollback(spDescomptes); // Rollback al savepoint
                            }
                            System.err.println("AVÍS: No s'han pogut aplicar els descomptes. Es continua sense ells. Causa: " + e.getMessage());
                        }

                        // --- AQUÍ HE ELIMINADO EL BLOQUE DUPLICADO QUE DABA ERROR ---
                        // -------------------------------------------------------------------------
                        // PAS 6: CALCULAR EL TOTAL FINAL I ACTUALITZAR LA COMANDA (VERSIÓ CORRECTA)
                        // -------------------------------------------------------------------------
                        // 6.1. Calculem la suma real de les línies
                        String sqlTotal = "SELECT SUM(quantitat * preuUnitari) FROM LiniesComanda WHERE comanda_id = ?";
                        double totalComanda = 0.0;

                        try (PreparedStatement psTotal = conn.prepareStatement(sqlTotal)) {
                            psTotal.setInt(1, comanda.getId());
                            try (ResultSet rsTotal = psTotal.executeQuery()) {
                                if (rsTotal.next()) {
                                    // FIX: Usem l'índex 1 per evitar l'error "Column not found"
                                    totalComanda = rsTotal.getDouble(1);
                                }
                            }
                        }

                        // 6.2. Actualitzem el camp 'total' a la taula Comandes
                        String sqlUpdateTotal = "UPDATE Comandes SET total = ? WHERE id = ?";
                        try (PreparedStatement psUpdateTotal = conn.prepareStatement(sqlUpdateTotal)) {
                            psUpdateTotal.setDouble(1, totalComanda);
                            psUpdateTotal.setInt(2, comanda.getId());
                            psUpdateTotal.executeUpdate();
                        }
                        // -------------------------------------------------------------------------

                    } else {
                        throw new SQLException("Error en crear la comanda, no s'ha obtingut l'ID.");
                    }
                }
            }

            conn.commit(); // CONFIRMAR TRANSACCIÓ

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

    private void aplicarDescomptes(Connection conn, int comandaId) throws SQLException {
        String sqlDescomptes = "SELECT d.tipus, d.valor, lc.producte_id, lc.id AS linia_id, lc.preuUnitari "
                + "FROM LiniesComanda lc "
                + "JOIN Descomptes d ON lc.producte_id = d.producte_id "
                + "WHERE lc.comanda_id = ?";

        try (PreparedStatement psDescomptes = conn.prepareStatement(sqlDescomptes)) {
            psDescomptes.setInt(1, comandaId);

            try (ResultSet rs = psDescomptes.executeQuery()) {
                while (rs.next()) {
                    int liniaId = rs.getInt("linia_id");
                    String tipus = rs.getString("tipus");
                    double valorDescompte = rs.getDouble("valor");
                    double preuActual = rs.getDouble("preuUnitari");
                    double nouPreu = preuActual;

                    if (tipus.equals("%")) {
                        nouPreu = preuActual * (1 - (valorDescompte / 100.0));
                    } else if (tipus.equals("€")) {
                        nouPreu = preuActual - valorDescompte;
                        if (nouPreu < 0) {
                            nouPreu = 0;
                        }
                    }

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
