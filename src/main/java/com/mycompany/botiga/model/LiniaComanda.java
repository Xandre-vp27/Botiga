/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.model;

/**
 *
 * @author alumnet
 */
public class LiniaComanda {
    private int id;
    private int comanda_id;
    private int producte_id;
    private int quantitat;
    private double preuUnitari;

    public LiniaComanda() {
    }

    public LiniaComanda(int id, int comanda_id, int producte_id, int quantitat, double preuUnitari) {
        this.id = id;
        this.comanda_id = comanda_id;
        this.producte_id = producte_id;
        this.quantitat = quantitat;
        this.preuUnitari = preuUnitari;
    }

    public int getId() {
        return id;
    }

    public int getComanda_id() {
        return comanda_id;
    }

    public int getProducte_id() {
        return producte_id;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public double getPreuUnitari() {
        return preuUnitari;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComanda_id(int comanda_id) {
        this.comanda_id = comanda_id;
    }

    public void setProducte_id(int producte_id) {
        this.producte_id = producte_id;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }

    public void setPreuUnitari(double preuUnitari) {
        this.preuUnitari = preuUnitari;
    }
    
    
}
