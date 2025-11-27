/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.botiga;

import com.mycompany.botiga.DAO.ClientDAO;
import com.mycompany.botiga.DAO.ComandaDAO;
import com.mycompany.botiga.DAO.ConsultesDAO;
import com.mycompany.botiga.DAO.ProducteDAO;
import com.mycompany.botiga.model.Client;
import com.mycompany.botiga.model.Comanda;
import com.mycompany.botiga.model.LiniaComanda;
import com.mycompany.botiga.model.Producte;
import com.mycompany.botiga.util.Connexio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author alumnet
 */
public class Botiga {

    // Instanciem els DAOs com a estàtics per utilitzar-los a tota l'App
    private static ClientDAO clientDAO = new ClientDAO();
    private static ProducteDAO producteDAO = new ProducteDAO();
    private static ComandaDAO comandaDAO = new ComandaDAO();
    private static ConsultesDAO consultesDAO = new ConsultesDAO();

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcio = 0;

        do {
            System.out.println("\n===== BOTIGA ONLINE (JDBC) =====");
            System.out.println("1. Gestionar Clients (Llistar/Crear)");
            System.out.println("2. Gestionar Productes (Llistar/Crear)");
            System.out.println("3. CREAR COMANDA (Transacció Completa)");
            System.out.println("4. Consultar Historial Client (JOIN)");
            System.out.println("0. Sortir");
            System.out.print("Selecciona una opció: ");

            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcio = -1;
            }

            try {
                switch (opcio) {
                    case 1:
                        menuClients();
                        break;
                    case 2:
                        menuProductes();
                        break;
                    case 3:
                        crearNovaComanda();
                        break;
                    case 4:
                        menuConsultes();
                        break;
                    case 0:
                        System.out.println("Tancant l'aplicació...");
                        break;
                    default:
                        System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.err.println("ERROR DE BASE DE DADES: " + e.getMessage());
                e.printStackTrace();
            }

        } while (opcio != 0);

        sc.close();
    }

    // --- MÈTODES DEL MENÚ ---
    private static void menuClients() throws SQLException {
        System.out.println("\n--- CLIENTS ---");
        System.out.println("1. Llistar tots");
        System.out.println("2. Crear nou client");
        System.out.print("Opció: ");
        int op = Integer.parseInt(sc.nextLine());

        if (op == 1) {
            List<Client> llista = clientDAO.llistar();
            for (Client c : llista) {
                System.out.println(c.getId() + " - " + c.getNom() + " (" + c.getCorreu() + ")");
            }
        } else if (op == 2) {
            System.out.print("Nom: ");
            String nom = sc.nextLine();
            System.out.print("Correu: ");
            String correu = sc.nextLine();
            Client c = new Client();
            c.setNom(nom);
            c.setCorreu(correu);

            clientDAO.insertar(c);
            System.out.println("Client creat correctament!");
        }
    }

    private static void menuProductes() throws SQLException {
        System.out.println("\n--- PRODUCTES ---");
        System.out.println("1. Llistar tots");
        System.out.println("2. Crear nou producte");
        System.out.print("Opció: ");
        int op = Integer.parseInt(sc.nextLine());

        if (op == 1) {
            List<Producte> llista = producteDAO.llistar();
            for (Producte p : llista) {
                System.out.println("ID: " + p.getId() + " | " + p.getNom() + " | Preu: " + p.getPreu() + "€ | Estoc: " + p.getEstoc());
            }
        } else if (op == 2) {
            System.out.print("Nom: ");
            String nom = sc.nextLine();
            System.out.print("Preu: ");
            double preu = Double.parseDouble(sc.nextLine());
            System.out.print("Estoc inicial: ");
            int estoc = Integer.parseInt(sc.nextLine());

            Producte p = new Producte();
            p.setNom(nom);
            p.setPreu(preu);
            p.setEstoc(estoc);

            producteDAO.insertar(p); // Compte: el teu mètode es diu 'insertar' o 'inserir'? Revisa ProducteDAO.
            System.out.println("Producte creat correctament!");
        }
    }

    private static void crearNovaComanda() {
        System.out.println("\n--- NOVA COMANDA ---");
        try {
            // 1. Identificar Client
            System.out.print("Introdueix l'ID del Client: ");
            int clientId = Integer.parseInt(sc.nextLine());

            // Creem l'objecte comanda base
            Comanda comanda = new Comanda();
            comanda.setClient_id(clientId);

            List<LiniaComanda> linies = new ArrayList<>();
            boolean afegirMes = true;

            // 2. Bucle per afegir productes
            while (afegirMes) {
                System.out.print("ID del Producte: ");
                int prodId = Integer.parseInt(sc.nextLine());

                System.out.print("Quantitat: ");
                int quantitat = Integer.parseInt(sc.nextLine());

                // --- CANVI AQUÍ: Usem el DAO en lloc del mètode auxiliar ---
                Producte producteTrobat = producteDAO.getById(prodId);

                if (producteTrobat == null) {
                    System.out.println("ERROR: Producte no trobat amb ID " + prodId);
                } else {
                    // Opcional: Podem avisar si no hi ha prou estoc abans d'enviar-ho a la transacció
                    if (producteTrobat.getEstoc() < quantitat) {
                        System.out.println("AVÍS: Estoc insuficient (Disponible: " + producteTrobat.getEstoc() + "). La transacció fallarà.");
                    }

                    LiniaComanda linia = new LiniaComanda();
                    linia.setProducte_id(prodId);
                    linia.setQuantitat(quantitat);
                    // Agafem el preu directament de l'objecte recuperat pel DAO
                    linia.setPreuUnitari(producteTrobat.getPreu());

                    linies.add(linia);
                    System.out.println("Línia afegida: " + producteTrobat.getNom());
                }
                // -----------------------------------------------------------

                System.out.print("Vols afegir un altre producte? (S/N): ");
                if (sc.nextLine().equalsIgnoreCase("N")) {
                    afegirMes = false;
                }
            }

            if (linies.isEmpty()) {
                System.out.println("Comanda cancel·lada: No hi ha productes.");
                return;
            }

            // 3. CRIDA A LA LÒGICA TRANSACCIONAL COMPLEXA
            System.out.println("Processant comanda...");
            comandaDAO.crearComanda(comanda, linies);
            System.out.println("✅ COMANDA REALITZADA AMB ÈXIT!");

        } catch (SQLException e) {
            System.out.println("❌ ERROR AL PROCESSAR LA COMANDA: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Dades numèriques invàlides.");
        }
    }

    private static void menuConsultes() throws SQLException {
        System.out.println("\n--- HISTORIAL DE COMANDES ---");
        System.out.print("Introdueix l'ID del Client a consultar: ");
        try {
            int clientId = Integer.parseInt(sc.nextLine());

            // Cridem al ConsultesDAO
            List<String> informe = consultesDAO.llistarComandesClient(clientId);

            System.out.println("\n RESULTATS:");
            for (String linia : informe) {
                System.out.println(linia);
            }

        } catch (NumberFormatException e) {
            System.out.println("ID invàlid.");
        }
    }

}
