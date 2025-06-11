// Recipe.java - Modello migliorato per le ricette
package com.catering.model;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Recipe rappresenta una ricetta completa, 
 * con metadati (tempo, porzioni, difficoltà), ingredienti e passi di preparazione.
 */
public class Recipe {
    
    // ===== Campi di stato =====
    
    private int id;                              // Identificativo univoco della ricetta
    private String name;                         // Nome della ricetta (es. "Tiramisù")
    private String description;                  // Descrizione breve o storia della ricetta
    private int preparationTime;                 // Tempo di preparazione in minuti
    private int cookingTime;                     // Tempo di cottura in minuti
    private int servings;                        // Numero di porzioni reso dalla ricetta
    private String difficulty;                   // Livello di difficoltà (es. "Facile", "Media", "Difficile")
    private List<String> ingredients;            // Elenco di ingredienti (ogni ingrediente come stringa)
    private List<Preparation> preparations;      // Lista di step di preparazione (oggetti Preparation)
    
    // ===== Costruttori =====
    
    /**
     * Costruttore di default.
     * Inizializza le liste di ingredienti e preparazioni vuote.
     * Utile per framework di serializzazione/deserializzazione.
     */
    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.preparations = new ArrayList<>();
    }
    
    /**
     * Costruttore parametrizzato.
     * Richiama il costruttore di default per inizializzare le liste,
     * poi valorizza nome e descrizione.
     *
     * @param name         Nome della ricetta
     * @param description  Descrizione breve della ricetta
     */
    public Recipe(String name, String description) {
        this();  // inizializza ingredients e preparations
        this.name = name;
        this.description = description;
    }
    
    // ===== Getters e Setters =====
    // Metodi per accedere e modificare i campi privati in modo controllato
    
    /** @return l'ID univoco della ricetta */
    public int getId() {
        return id;
    }
    
    /** Imposta l'ID univoco della ricetta */
    public void setId(int id) {
        this.id = id;
    }
    
    /** @return il nome della ricetta */
    public String getName() {
        return name;
    }
    
    /** Imposta il nome della ricetta */
    public void setName(String name) {
        this.name = name;
    }
    
    /** @return la descrizione della ricetta */
    public String getDescription() {
        return description;
    }
    
    /** Imposta la descrizione della ricetta */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** @return il tempo di preparazione (minuti) */
    public int getPreparationTime() {
        return preparationTime;
    }
    
    /** Imposta il tempo di preparazione (minuti) */
    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
    
    /** @return il tempo di cottura (minuti) */
    public int getCookingTime() {
        return cookingTime;
    }
    
    /** Imposta il tempo di cottura (minuti) */
    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
    
    /** @return il numero di porzioni */
    public int getServings() {
        return servings;
    }
    
    /** Imposta il numero di porzioni */
    public void setServings(int servings) {
        this.servings = servings;
    }
    
    /** @return il livello di difficoltà */
    public String getDifficulty() {
        return difficulty;
    }
    
    /** Imposta il livello di difficoltà */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    /** @return la lista corrente di ingredienti */
    public List<String> getIngredients() {
        return ingredients;
    }
    
    /**
     * Sostituisce la lista di ingredienti.
     * Nota: sovrascrive completamente quella esistente.
     *
     * @param ingredients  Nuova lista di stringhe ingredienti
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    
    /**
     * Aggiunge un singolo ingrediente alla ricetta.
     *
     * @param ingredient  Stringa descrittiva dell'ingrediente (es. "200g farina")
     */
    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }
    
    /** @return la lista corrente degli step di preparazione */
    public List<Preparation> getPreparations() {
        return preparations;
    }
    
    /**
     * Sostituisce la lista di preparazioni.
     * Nota: sovrascrive completamente quella esistente.
     *
     * @param preparations  Nuova lista di oggetti Preparation
     */
    public void setPreparations(List<Preparation> preparations) {
        this.preparations = preparations;
    }
    
    /**
     * Aggiunge uno step di preparazione alla ricetta.
     *
     * @param preparation  Oggetto Preparation con numero, descrizione e tempo
     */
    public void addPreparation(Preparation preparation) {
        this.preparations.add(preparation);
    }
    
    // ===== Metodi di utilità =====
    
    /**
     * Override di toString per restituire una rappresentazione compatta della ricetta.
     * Qui restituisce solo il nome, ma si potrebbe estendere per includere tempi o difficoltà.
     *
     * @return nome della ricetta
     */
    @Override
    public String toString() {
        return name;
    }
}
