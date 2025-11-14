/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.model;

/**
 *
 * @author alumnet
 */


public class Descompte {
    private int id;
    private int producte_id;
    private TipusDescompte tipus;
    private double valor;     

    public Descompte() {
    }

    public Descompte(int id, int producte_id, TipusDescompte tipus, double valor) {
        this.id = id;
        this.producte_id = producte_id;
        this.tipus = tipus;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getProducte_id() {
        return producte_id;
    }

    public TipusDescompte getTipus() {
        return tipus;
    }

    public double getValor() {
        return valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProducte_id(int producte_id) {
        this.producte_id = producte_id;
    }

    public void setTipus(TipusDescompte tipus) {
        this.tipus = tipus;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    
}
