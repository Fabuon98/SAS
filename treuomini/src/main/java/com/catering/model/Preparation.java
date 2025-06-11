// Preparation.java - Modello per i passaggi di preparazione
package com.catering.model;

public class Preparation {
    private int id;
    private int stepNumber;
    private String description;
    private int timeInMinutes;
    
    public Preparation() {}
    
    public Preparation(int stepNumber, String description, int timeInMinutes) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.timeInMinutes = timeInMinutes;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getTimeInMinutes() { return timeInMinutes; }
    public void setTimeInMinutes(int timeInMinutes) { this.timeInMinutes = timeInMinutes; }
    
    @Override
    public String toString() {
        return "Step " + stepNumber + ": " + description + " (" + timeInMinutes + " min)";
    }
}