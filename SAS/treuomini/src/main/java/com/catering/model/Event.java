// Event.java - Modello per gli eventi
package com.catering.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private int numberOfGuests;
    private List<Menu> menus;
    
    public Event() {
        this.menus = new ArrayList<>();
    }
    
    public Event(String name, LocalDate date, String location, int numberOfGuests) {
        this();
        this.name = name;
        this.date = date;
        this.location = location;
        this.numberOfGuests = numberOfGuests;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    
    public List<Menu> getMenus() { return menus; }
    public void setMenus(List<Menu> menus) { this.menus = menus; }
    
    public void addMenu(Menu menu) {
        this.menus.add(menu);
    }
    
    @Override
    public String toString() {
        return name + " - " + date + " (" + numberOfGuests + " ospiti)";
    }
}