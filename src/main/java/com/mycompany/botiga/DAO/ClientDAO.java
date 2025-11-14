/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.DAO;

import com.mycompany.botiga.model.Client;
import com.mycompany.botiga.util.Connexio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumnet
 */
public class ClientDAO {

    public void insertar(Client c) throws SQLException {
        String sql = "INSERT INTO Clients (nom, correu) VALUES (?, ?)";

        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNom());
            ps.setString(2, c.getCorreu());

            ps.executeUpdate();
        }
    }
    
    public List<Client> llistar() throws SQLException {
        List<Client> llista = new ArrayList<>();
        String sql = "SELECT * FROM Clients";
        
        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setCorreu(rs.getString("correu"));
                
                llista.add(c);
            }
        }
        
        return llista;
    }
    
    public void actualitzar(Client c) throws SQLException {
        String sql = "UPDATE Clients SET nom = ?, correu = ? WHERE id = ?";
        
        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setString(2, c.getCorreu());
            ps.setInt(3, c.getId());
            
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Clients WHERE id = ?";
        
        try (Connection conn = Connexio.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
}
