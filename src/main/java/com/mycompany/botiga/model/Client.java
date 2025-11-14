/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.model;

/**
 *
 * @author alumnet
 */
public class Client {
    private int id;
    private String nom;
    private String correu;

    public Client() {
    }

    public Client(int id, String nom, String correu) {
        this.id = id;
        this.nom = nom;
        this.correu = correu;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCorreu() {
        return correu;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }

}
