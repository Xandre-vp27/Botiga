/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.botiga;

import com.mycompany.botiga.util.Connexio;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author alumnet
 */
public class Botiga {

    public static void main(String[] args) {
        try (Connection conn = Connexio.getConnection()) {
            System.out.println("Conexión correcta a la base de datos Botiga!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
