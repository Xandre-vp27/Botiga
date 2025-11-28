/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.botiga.model;

/**
 *
 * @author alumnet
 */
public enum TipusDescompte {
    PERCENT("%"), 
    EURO("€");   
    
    private final String dbValue;

    TipusDescompte(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
    
    // Método estático para obtener el Enum desde el String de la BD
    public static TipusDescompte fromDbValue(String value) {
        for (TipusDescompte t : TipusDescompte.values()) {
            if (t.dbValue.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de Descuento desconocido: " + value);
    }
}