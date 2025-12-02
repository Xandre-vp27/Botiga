package com.mycompany.botiga;

import com.mycompany.botiga.DAO.ClientDAO;
import com.mycompany.botiga.DAO.ComandaDAO;
import com.mycompany.botiga.DAO.ConsultesDAO;
import com.mycompany.botiga.DAO.DescompteDAO; // Nova importació
import com.mycompany.botiga.DAO.ProducteDAO;
import com.mycompany.botiga.model.Client;
import com.mycompany.botiga.model.Comanda;
import com.mycompany.botiga.model.Descompte; // Nova importació
import com.mycompany.botiga.model.LiniaComanda;
import com.mycompany.botiga.model.Producte;
import com.mycompany.botiga.model.TipusDescompte; // Nova importació
import com.mycompany.botiga.util.Connexio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Botiga {

    private static ClientDAO clientDAO = new ClientDAO();
    private static ProducteDAO producteDAO = new ProducteDAO();
    private static ComandaDAO comandaDAO = new ComandaDAO();
    private static ConsultesDAO consultesDAO = new ConsultesDAO();
    private static DescompteDAO descompteDAO = new DescompteDAO();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcio = 0;

        do {
            System.out.println("\n===== BOTIGA ONLINE =====");
            System.out.println("1. Gestionar Clients");
            System.out.println("2. Gestionar Productes");
            System.out.println("3. Gestionar Descomptes");
            System.out.println("4. CREAR COMANDA");
            System.out.println("5. Consultar Historial Client");
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
                        menuDescomptes();
                        break;
                    case 4:
                        crearNovaComanda();
                        break;
                    case 5:
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
            } catch (NumberFormatException e) {
                System.err.println("Error de dades: Has d'introduir un número vàlid.");
            }

        } while (opcio != 0);

        sc.close();
    }

    // Mètode auxiliar per capturar el tipus de descompte
    private static TipusDescompte getTipusDescompteFromInput() {
        System.out.println("Tipus de descompte:");
        System.out.println("1. Percentatge (%)");
        System.out.println("2. Fixe (€)");
        System.out.print("Selecciona (1/2): ");
        int tipusOp = Integer.parseInt(sc.nextLine());

        if (tipusOp == 1) {
            return TipusDescompte.PERCENT;
        } else if (tipusOp == 2) {
            return TipusDescompte.EURO;
        } else {
            throw new NumberFormatException("Opció de tipus de descompte invàlida.");
        }
    }

    // --- MÈTODE CLIENTS ---
    private static void menuClients() throws SQLException {
        System.out.println("\n--- CLIENTS (CRUD) ---");
        System.out.println("1. Llistar tots");
        System.out.println("2. Crear nou client");
        System.out.println("3. Modificar client");
        System.out.println("4. Eliminar client");
        System.out.print("Opció: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1: // Llistar
                List<Client> llista = clientDAO.llistar();
                for (Client c : llista) {
                    System.out.println(c.getId() + " - " + c.getNom() + " (" + c.getCorreu() + ")");
                }
                break;
            case 2: // Crear
                System.out.print("Nom: ");
                String nom = sc.nextLine();
                System.out.print("Correu: ");
                String correu = sc.nextLine();
                Client c = new Client();
                c.setNom(nom);
                c.setCorreu(correu);
                clientDAO.insertar(c);
                System.out.println("Client creat correctament!");
                break;
            case 3: // Modificar
                System.out.print("ID del client a modificar: ");
                int idMod = Integer.parseInt(sc.nextLine());
                System.out.print("Nou Nom: ");
                String nouNom = sc.nextLine();
                System.out.print("Nou Correu: ");
                String nouCorreu = sc.nextLine();
                Client cMod = new Client(idMod, nouNom, nouCorreu);
                clientDAO.actualitzar(cMod);
                System.out.println("Client actualitzat correctament!");
                break;
            case 4: // Eliminar
                System.out.print("ID del client a eliminar: ");
                int idDel = Integer.parseInt(sc.nextLine());
                clientDAO.eliminar(idDel);
                System.out.println("Client eliminat correctament!");
                break;
            default:
                System.out.println("Opció invàlida.");
        }
    }

    // --- MÈTODE PRODUCTES ---
    private static void menuProductes() throws SQLException {
        System.out.println("\n--- PRODUCTES (CRUD) ---");
        System.out.println("1. Llistar tots");
        System.out.println("2. Crear nou producte");
        System.out.println("3. Modificar producte");
        System.out.println("4. Eliminar producte");
        System.out.print("Opció: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1: // Llistar
                List<Producte> llista = producteDAO.llistar();
                for (Producte p : llista) {
                    System.out.println("ID: " + p.getId() + " | " + p.getNom() + " | Preu: " + p.getPreu()
                            + "€ | Estoc: " + p.getEstoc());
                }
                break;
            case 2: // Crear
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

                producteDAO.insertar(p);
                System.out.println("Producte creat correctament!");
                break;
            case 3: // Modificar
                System.out.print("ID del producte a modificar: ");
                int idMod = Integer.parseInt(sc.nextLine());
                System.out.print("Nou Nom: ");
                String nouNom = sc.nextLine();
                System.out.print("Nou Preu: ");
                double nouPreu = Double.parseDouble(sc.nextLine());
                System.out.print("Nou Estoc: ");
                int nouEstoc = Integer.parseInt(sc.nextLine());

                Producte pMod = new Producte(idMod, nouNom, nouPreu, nouEstoc);
                producteDAO.actualitzar(pMod);
                System.out.println("Producte actualitzat correctament!");
                break;
            case 4: // Eliminar
                System.out.print("ID del producte a eliminar: ");
                int idDel = Integer.parseInt(sc.nextLine());
                producteDAO.eliminar(idDel);
                System.out.println("Producte eliminat correctament!");
                break;
            default:
                System.out.println("Opció invàlida.");
        }
    }

    // --- MÈTODE DESCOMPTES ---
    private static void menuDescomptes() throws SQLException {
        System.out.println("\n--- DESCOMPTES (CRUD) ---");
        System.out.println("1. Llistar tots");
        System.out.println("2. Crear nou descompte");
        System.out.println("3. Modificar descompte");
        System.out.println("4. Eliminar descompte");
        System.out.print("Opció: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1: // Llistar
                List<Descompte> llista = descompteDAO.llistar();
                for (Descompte d : llista) {
                    System.out.println("ID: " + d.getId() + " | Producte ID: " + d.getProducte_id() +
                            " | Tipus: " + d.getTipus() + " | Valor: " + d.getValor());
                }
                break;
            case 2: // Crear
                System.out.print("ID del Producte al que aplicar el descompte: ");
                int prodId = Integer.parseInt(sc.nextLine());
                TipusDescompte tipusCrear = getTipusDescompteFromInput();
                System.out.print("Valor del descompte: ");
                double valorCrear = Double.parseDouble(sc.nextLine());

                Descompte dCrear = new Descompte();
                dCrear.setProducte_id(prodId);
                dCrear.setTipus(tipusCrear);
                dCrear.setValor(valorCrear);
                descompteDAO.insertar(dCrear);
                System.out.println("Descompte creat correctament!");
                break;
            case 3: // Modificar
                System.out.print("ID del Descompte a modificar: ");
                int idMod = Integer.parseInt(sc.nextLine());
                System.out.print("Nou ID del Producte: ");
                int nouProdId = Integer.parseInt(sc.nextLine());
                TipusDescompte tipusMod = getTipusDescompteFromInput();
                System.out.print("Nou Valor del descompte: ");
                double nouValor = Double.parseDouble(sc.nextLine());

                Descompte dMod = new Descompte(idMod, nouProdId, tipusMod, nouValor);
                descompteDAO.actualitzar(dMod);
                System.out.println("Descompte actualitzat correctament!");
                break;
            case 4: // Eliminar
                System.out.print("ID del Descompte a eliminar: ");
                int idDel = Integer.parseInt(sc.nextLine());
                descompteDAO.eliminar(idDel);
                System.out.println("Descompte eliminat correctament!");
                break;
            default:
                System.out.println("Opció invàlida.");
        }
    }

    // --- MÈTODE CREAR COMANDA ---
    private static void crearNovaComanda() {
        System.out.println("\n--- NOVA COMANDA ---");
        try {
            // 1. Identificar Client
            System.out.print("Introdueix l'ID del Client: ");
            int clientId = Integer.parseInt(sc.nextLine());
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

                Producte producteTrobat = producteDAO.getById(prodId);

                if (producteTrobat == null) {
                    System.out.println("ERROR: Producte no trobat amb ID " + prodId);
                } else {
                    if (producteTrobat.getEstoc() < quantitat) {
                        System.out.println("AVÍS: Estoc insuficient (Disponible: " + producteTrobat.getEstoc()
                                + "). La transacció fallarà si s'envia.");
                    }

                    LiniaComanda linia = new LiniaComanda();
                    linia.setProducte_id(prodId);
                    linia.setQuantitat(quantitat);
                    linia.setPreuUnitari(producteTrobat.getPreu());

                    linies.add(linia);
                    System.out.println("Línia afegida: " + producteTrobat.getNom());
                }

                System.out.print("Vols afegir un altre producte? (S/N): ");
                if (sc.nextLine().equalsIgnoreCase("N")) {
                    afegirMes = false;
                }
            }

            if (linies.isEmpty()) {
                System.out.println("Comanda cancel·lada: No hi ha productes.");
                return;
            }

            // 3. S'inserta finalment la comanda a la BBDD
            System.out.println("Processant comanda...");
            comandaDAO.crearComanda(comanda, linies);
            System.out.println("✅ COMANDA REALITZADA AMB ÈXIT!");

        } catch (SQLException e) {
            System.out.println("❌ ERROR AL PROCESSAR LA COMANDA: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Dades numèriques invàlides.");
        }
    }

    // --- CONSULTAR COMANDES ---
    private static void menuConsultes() throws SQLException {
        System.out.println("\n--- HISTORIAL DE COMANDES ---");
        System.out.print("Introdueix l'ID del Client a consultar: ");
        try {
            int clientId = Integer.parseInt(sc.nextLine());
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