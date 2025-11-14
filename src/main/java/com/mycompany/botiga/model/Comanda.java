/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.botiga.model;

import java.sql.Timestamp;

/**
 *
 * @author alumnet
 */
public class Comanda {
    private int id;
    private int client_id;
    private Timestamp data;
    private double total;

    public Comanda() {
    }

    public Comanda(int id, int client_id, Timestamp data, double total) {
        this.id = id;
        this.client_id = client_id;
        this.data = data;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public int getClient_id() {
        return client_id;
    }

    public Timestamp getData() {
        return data;
    }

    public double getTotal() {
        return total;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
