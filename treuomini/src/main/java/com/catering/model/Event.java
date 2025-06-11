// Event.java - Modello per gli eventi
package com.catering.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Event rappresenta un evento di catering,
 * con nome, data, luogo, numero di invitati e lista di menu associati.
 */
public class Event {
    
    // ===== Campi di stato =====
    
    private int id;                                  // Identificativo univoco dell'evento
    private String name;                             // Nome descrittivo dell'evento
    private LocalDate date;                          // Data in cui si svolge l'evento
    private String location;                         // Luogo/indirizzo dell'evento
    private int numberOfGuests;                      // Numero di invitati previsti
    private List<Menu> menus;                        // Lista dei menu assegnati all'evento
    
    // ===== Costruttori =====
    
    /**
     * Costruttore di default. Inizializza la lista dei menu vuota.
     * Utile per framework di serializzazione/deserializzazione.
     */
    public Event() {
        this.menus = new ArrayList<>();              // Inizializza la collezione di menu
    }
    
    /**
     * Costruttore parametrizzato.
     * Richiama il costruttore di default per inizializzare la lista,
     * poi imposta i campi principali dell'evento.
     *
     * @param name              Nome dell'evento
     * @param date              Data dell'evento (LocalDate)
     * @param location          Luogo dell'evento
     * @param numberOfGuests    Numero di ospiti previsti
     */
    public Event(String name, LocalDate date, String location, int numberOfGuests) {
        this();                                      // Chiama Event() per inizializzare menus
        this.name = name;
        this.date = date;
        this.location = location;
        this.numberOfGuests = numberOfGuests;
    }
    
    // ===== Getters e Setters =====
    // Metodi per accedere e modificare i campi privati
    
    /** @return l'ID univoco dell'evento */
    public int getId() {
        return id;
    }
    
    /** Imposta l'ID univoco dell'evento */
    public void setId(int id) {
        this.id = id;
    }
    
    /** @return il nome dell'evento */
    public String getName() {
        return name;
    }
    
    /** Imposta il nome dell'evento */
    public void setName(String name) {
        this.name = name;
    }
    
    /** @return la data dell'evento */
    public LocalDate getDate() {
        return date;
    }
    
    /** Imposta la data dell'evento */
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    /** @return il luogo dell'evento */
    public String getLocation() {
        return location;
    }
    
    /** Imposta il luogo dell'evento */
    public void setLocation(String location) {
        this.location = location;
    }
    
    /** @return il numero di ospiti previsto */
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    /** Imposta il numero di ospiti previsto */
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    /** @return la lista corrente di menu assegnati all'evento */
    public List<Menu> getMenus() {
        return menus;
    }
    
    /**
     * Imposta una nuova lista di menu.
     * Nota: sostituisce completamente la collezione esistente.
     *
     * @param menus  Nuova lista di Menu
     */
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
    
    // ===== Metodi di utilità =====
    
    /**
     * Aggiunge un singolo menu all'evento.
     * Non controlla duplicati: se lo stesso menu viene aggiunto due volte,
     * comparirà due volte nella lista.
     *
     * @param menu  Menu da associare all'evento
     */
    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }
    
    /**
     * Rappresentazione testuale semplificata dell'evento,
     * utile per logging o debug.
     * Formato: "<nome> - <data> (<numeroOspiti> ospiti)"
     */
    @Override
    public String toString() {
        return name
            + " - "
            + date
            + " ("
            + numberOfGuests
            + " ospiti)";
    }
}
