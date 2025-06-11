// Menu.java - Modello per i menu
package com.catering.model;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Menu rappresenta un menù di catering,
 * con nome, descrizione, prezzo e la lista delle sezioni che lo compongono.
 */
public class Menu {
    
    // ===== Campi di stato =====
    
    private int id;                        // Identificativo univoco del menù
    private String name;                   // Nome del menù (es. "Menu Vegetariano")
    private String description;            // Descrizione dettagliata del menù
    private double price;                  // Prezzo complessivo del menù
    private List<Section> sections;        // Lista delle sezioni (categorie di piatti) incluse
    
    // ===== Costruttori =====
    
    /**
     * Costruttore di default.
     * Inizializza la lista di sezioni vuota.
     * Utile per framework di serializzazione o per creare oggetti vuoti da popolare successivamente.
     */
    public Menu() {
        this.sections = new ArrayList<>(); // Inizializza la collezione di sezioni
    }
    
    /**
     * Costruttore parametrizzato.
     * Richiama il costruttore di default per inizializzare la lista,
     * poi valorizza i campi con i parametri forniti.
     *
     * @param name         Nome del menù
     * @param description  Descrizione del menù
     * @param price        Prezzo del menù
     */
    public Menu(String name, String description, double price) {
        this();                             // Chiama Menu() per inizializzare sections
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // ===== Getters e Setters =====
    // Forniscono accesso controllato ai campi privati
    
    /** @return l'ID univoco del menù */
    public int getId() {
        return id;
    }
    
    /** Imposta l'ID univoco del menù */
    public void setId(int id) {
        this.id = id;
    }
    
    /** @return il nome del menù */
    public String getName() {
        return name;
    }
    
    /** Imposta il nome del menù */
    public void setName(String name) {
        this.name = name;
    }
    
    /** @return la descrizione del menù */
    public String getDescription() {
        return description;
    }
    
    /** Imposta la descrizione del menù */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** @return il prezzo del menù */
    public double getPrice() {
        return price;
    }
    
    /** Imposta il prezzo del menù */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /** @return la lista corrente di sezioni del menù */
    public List<Section> getSections() {
        return sections;
    }
    
    /**
     * Sostituisce la lista di sezioni.
     * Nota: sovrascrive completamente la collezione esistente.
     *
     * @param sections  Nuova lista di Section
     */
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
    
    // ===== Metodi di utilità =====
    
    /**
     * Aggiunge una singola sezione al menù.
     * Non verifica duplicati: se si aggiunge più volte la stessa istanza,
     * comparirà più volte nella lista.
     *
     * @param section  Sezione da aggiungere al menù
     */
    public void addSection(Section section) {
        this.sections.add(section);
    }
    
    /**
     * Override di toString per una rappresentazione sintetica del menù,
     * utile per logging o debug.
     * Formato: "<nome> - €<prezzo>"
     *
     * @return stringa descrittiva del menù
     */
    @Override
    public String toString() {
        return name
            + " - €"
            + price;
    }
}
