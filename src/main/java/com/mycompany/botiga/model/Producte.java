/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.model;

/**
 *
 * @author alumnet
 */
public class Producte {
    private int id;
    private String nom;
    private double preu;
    private int estoc;

    public Producte() {
    }

    public Producte(int id, String nom, double preu, int estoc) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.estoc = estoc;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public double getPreu() {
        return preu;
    }

    public int getEstoc() {
        return estoc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public void setEstoc(int estoc) {
        this.estoc = estoc;
    }
    
    
}
