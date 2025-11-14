/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.model.Descompte;
import com.mycompany.botiga.model.TipusDescompte;
import java.util.List;
import com.mycompany.botiga.util.Connexio;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author alumnet
 */
public class DescompteDAO {

    public void insertar(Descompte d) throws SQLException {
        String sql = "INSERT INTO Descomptes (producte_id, tipus, valor) VALUES (?, ?, ?)";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProducte_id());
            
            //Hem de comprovar quin TipusDescompte es per ficar % o €
            if (d.getTipus() == TipusDescompte.PERCENT) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, "€");
            }
            ps.setDouble(3, d.getValor());

            ps.executeUpdate();
        }
    }

    public List<Descompte> llistar() throws SQLException {
        String sql = "SELECT * FROM Descomptes";
        List<Descompte> llista = new ArrayList<>();

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Descompte d = new Descompte();

                d.setId(rs.getInt("id"));
                d.setProducte_id(rs.getInt("producte_id"));
                
                //Mirem primer que tipus de descompte es per poder fer el .setTipus()
                String t = rs.getString("tipus");  // '%' o '€'
                TipusDescompte tipus;
                if (t.equals("%")) {
                    tipus = TipusDescompte.PERCENT;
                } else {
                    tipus = TipusDescompte.EURO;
                }
                d.setTipus(tipus);
                d.setValor(rs.getDouble("valor")); 
                
                llista.add(d);
            }
        }
        
        return llista;
    }

    public void actualitzar(Descompte d) throws SQLException {
        String sql = "UPDATE Descomptes SET producte_id = ?, tipus = ?, valor = ? WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getProducte_id());
            
            //Hem de comprovar quin TipusDescompte es per ficar % o €
            if (d.getTipus() == TipusDescompte.PERCENT) {
                ps.setString(2, "%");
            } else {
                ps.setString(2, "€");
            }
            ps.setDouble(3, d.getValor());
            ps.setInt(4, d.getId());

            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Descomptes WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }
}
