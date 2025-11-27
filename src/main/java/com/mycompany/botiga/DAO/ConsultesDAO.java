/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.util.Connexio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumnet
 */
public class ConsultesDAO {
    
    

    public List<String> llistarComandesClient(int clientId) throws SQLException {
        List<String> informe = new ArrayList<>();

        // Consulta JOIN que uneix Comandes, LiniesComanda i Productes
        String sql = "SELECT "
                + "c.id AS comanda_id, c.data, c.total AS comanda_total, "
                + "lc.quantitat, lc.preuUnitari, p.nom AS producte_nom "
                + "FROM Comandes c "
                + "JOIN LiniesComanda lc ON c.id = lc.comanda_id "
                + "JOIN Productes p ON lc.producte_id = p.id "
                + "WHERE c.client_id = ? "
                + "ORDER BY c.id, p.nom";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {

                int comandaAnterior = -1; // Usat per detectar un canvi de comanda

                while (rs.next()) {
                    int comandaActual = rs.getInt("comanda_id");

                    // Si és una nova comanda, afegim l'encapçalament i el total
                    if (comandaActual != comandaAnterior) {
                        double total = rs.getDouble("comanda_total");
                        String data = rs.getString("data");

                        informe.add("--------------------------------------------------");
                        informe.add(String.format("COMANDA #%d | Data: %s | TOTAL FINAL: %.2f€",
                                comandaActual, data, total));
                        informe.add("--------------------------------------------------");
                        comandaAnterior = comandaActual;
                    }

                    // Detall de la línia de comanda
                    int quantitat = rs.getInt("quantitat");
                    double preuUnitari = rs.getDouble("preuUnitari");
                    String nomProducte = rs.getString("producte_nom");

                    informe.add(String.format("  - %d x %s (Preu/Unitat: %.2f€)",
                            quantitat, nomProducte, preuUnitari));
                }

                if (informe.isEmpty()) {
                    informe.add("No s'han trobat comandes per al client ID " + clientId);
                }
            }
        }

        return informe;
    }
}
