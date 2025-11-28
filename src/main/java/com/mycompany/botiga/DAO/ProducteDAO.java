/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.model.Producte;
import com.mycompany.botiga.util.Connexio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumnet
 */
public class ProducteDAO {

    public void insertar(Producte p) throws SQLException {
        String sql = "INSERT INTO Productes (nom, preu, estoc) VALUES (?, ?, ?)";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPreu());
            ps.setInt(3, p.getEstoc());

            ps.executeUpdate();
        }
    }

    public List<Producte> llistar() throws SQLException {
        String sql = "SELECT * FROM Productes";
        List<Producte> llista = new ArrayList<>();

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producte p = new Producte();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPreu(rs.getDouble("preu"));
                p.setEstoc(rs.getInt("estoc"));

                llista.add(p);
            }
        }

        return llista;
    }

    public void actualitzar(Producte p) throws SQLException {
        String sql = "UPDATE Productes SET nom = ?, preu = ?, estoc = ? WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPreu());
            ps.setInt(3, p.getEstoc());
            ps.setInt(4, p.getId());

            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Productes WHERE id = ?";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }

    public Producte getById(int id) throws SQLException {
        String sql = "SELECT * FROM Productes WHERE id = ?";
        Producte p = null;

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producte();
                    p.setId(rs.getInt("id"));
                    p.setNom(rs.getString("nom"));
                    p.setPreu(rs.getDouble("preu"));
                    p.setEstoc(rs.getInt("estoc"));
                }
            }
        }
        return p;
    }
}
