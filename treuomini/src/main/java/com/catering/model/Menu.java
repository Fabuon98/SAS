// Menu.java - Modello per i menu
package com.catering.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private int id;
    private String name;
    private String description;
    private double price;
    private List<Section> sections;
    
    public Menu() {
        this.sections = new ArrayList<>();
    }
    
    public Menu(String name, String description, double price) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }
    
    public void addSection(Section section) {
        this.sections.add(section);
    }
    
    @Override
    public String toString() {
        return name + " - €" + price;
    }
}