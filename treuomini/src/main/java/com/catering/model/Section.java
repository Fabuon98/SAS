// Section.java - Modello per le sezioni del menu
package com.catering.model;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Section rappresenta una categoria o porzione del menù,
 * ad esempio "Antipasti", "Primi Piatti", ecc.
 * Ogni sezione contiene un elenco di ricette associate.
 */
public class Section {

    // ===== Campi di stato =====

    private int id;                  // Identificativo univoco della sezione (assegnato dal Controller)
    private String name;             // Nome descrittivo della sezione (es. "Dolci")
    private String description;      // Descrizione estesa della sezione
    private List<Recipe> recipes;    // Lista delle ricette incluse in questa sezione

    // ===== Costruttori =====

    /**
     * Costruttore di default.
     * Inizializza la lista di ricette vuota.
     * Utile per framework di serializzazione/deserializzazione.
     */
    public Section() {
        this.recipes = new ArrayList<>();  // Evitiamo di avere una lista nulla
    }

    /**
     * Costruttore parametrizzato.
     * Richiama il costruttore di default per inizializzare la lista,
     * poi imposta nome e descrizione.
     *
     * @param name         Nome della sezione
     * @param description  Descrizione dettagliata della sezione
     */
    public Section(String name, String description) {
        this();                  // Inizializza recipes con ArrayList vuota
        this.name = name;        // Imposta il nome
        this.description = description;  // Imposta la descrizione
    }

    // ===== Getters e Setters =====
    // Consentono l'accesso controllato ai campi privati

    /** @return l'ID univoco della sezione */
    public int getId() {
        return id;
    }

    /** Imposta l'ID univoco della sezione */
    public void setId(int id) {
        this.id = id;
    }

    /** @return il nome della sezione */
    public String getName() {
        return name;
    }

    /** Imposta il nome della sezione */
    public void setName(String name) {
        this.name = name;
    }

    /** @return la descrizione della sezione */
    public String getDescription() {
        return description;
    }

    /** Imposta la descrizione della sezione */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return la lista corrente di ricette */
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Sostituisce completamente la lista di ricette.
     * Attenzione: sovrascrive ogni ricetta presente.
     *
     * @param recipes Nuova lista di oggetti Recipe
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    // ===== Metodi di utilità =====

    /**
     * Aggiunge una singola ricetta alla sezione.
     * Non effettua controlli di duplicazione: la stessa ricetta
     * può essere aggiunta più volte se è la stessa istanza.
     *
     * @param recipe Oggetto Recipe da aggiungere
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Override di toString per una rappresentazione sintetica della sezione.
     * Ritorna solo il nome, utile in logging, debug o in liste.
     *
     * @return il nome della sezione
     */
    @Override
    public String toString() {
        return name;
    }
}
