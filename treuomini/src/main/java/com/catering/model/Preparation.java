// Preparation.java - Modello per i passaggi di preparazione
package com.catering.model;

/**
 * La classe Preparation rappresenta un singolo step di preparazione
 * in una ricetta, con numero d'ordine, descrizione e tempo stimato.
 */
public class Preparation {
    
    // ===== Campi di stato =====
    
    private int id;                // Identificativo univoco dello step (assegnato da Controller)
    private int stepNumber;        // Ordine dello step nella sequenza (1, 2, 3, …)
    private String description;    // Descrizione testuale dell'operazione da eseguire
    private int timeInMinutes;     // Durata stimata in minuti per completare questo step
    
    // ===== Costruttori =====
    
    /**
     * Costruttore di default.
     * Utile per framework di serializzazione/deserializzazione.
     * Non inizializza campi, che vanno quindi impostati via setter.
     */
    public Preparation() {}
    
    /**
     * Costruttore parametrizzato.
     * Imposta il numero di step, la descrizione e il tempo.
     * L'id verrà assegnato successivamente dal Controller gestore.
     *
     * @param stepNumber     Posizione di questo step nella ricetta
     * @param description    Testo descrittivo dell'azione
     * @param timeInMinutes  Durata stimata in minuti
     */
    public Preparation(int stepNumber, String description, int timeInMinutes) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.timeInMinutes = timeInMinutes;
    }
    
    // ===== Getters e Setters =====
    // Permettono di leggere e modificare in modo controllato i campi privati
    
    /** @return l'id univoco dello step */
    public int getId() {
        return id;
    }
    
    /** Imposta l'id univoco dello step */
    public void setId(int id) {
        this.id = id;
    }
    
    /** @return il numero di ordine dello step */
    public int getStepNumber() {
        return stepNumber;
    }
    
    /** Imposta il numero di ordine dello step */
    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }
    
    /** @return la descrizione testuale dello step */
    public String getDescription() {
        return description;
    }
    
    /** Imposta la descrizione testuale dello step */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** @return la durata stimata in minuti */
    public int getTimeInMinutes() {
        return timeInMinutes;
    }
    
    /** Imposta la durata stimata in minuti */
    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }
    
    // ===== Metodi di utilità =====
    
    /**
     * Override di toString per una rappresentazione compatta e leggibile
     * di uno step di preparazione.
     * Formato: "Step <n>: <descrizione> (<tempo> min)"
     *
     * @return stringa descrittiva dello step
     */
    @Override
    public String toString() {
        return "Step " 
            + stepNumber 
            + ": " 
            + description 
            + " (" 
            + timeInMinutes 
            + " min)";
    }
}
