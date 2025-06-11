// Recipe.java - Modello migliorato per le ricette
package com.catering.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private int preparationTime;
    private int cookingTime;
    private int servings;
    private String difficulty;
    private List<String> ingredients;
    private List<Preparation> preparations;
    
    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.preparations = new ArrayList<>();
    }
    
    public Recipe(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }
    
    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }
    
    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    
    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }
    
    public List<Preparation> getPreparations() { return preparations; }
    public void setPreparations(List<Preparation> preparations) { this.preparations = preparations; }
    
    public void addPreparation(Preparation preparation) {
        this.preparations.add(preparation);
    }
    
    @Override
    public String toString() {
        return name;
    }
}