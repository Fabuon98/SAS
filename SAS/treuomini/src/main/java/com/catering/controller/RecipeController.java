// RecipeController.java - Gestione delle ricette
package com.catering.controller;

import com.catering.model.Recipe;
import com.catering.model.Preparation;
import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    private List<Recipe> recipes;
    private int nextRecipeId = 1;
    private int nextPreparationId = 1;
    
    public RecipeController() {
        this.recipes = new ArrayList<>();
        initializeDefaultRecipes();
    }
    
    // Inizializza ricette di esempio
    private void initializeDefaultRecipes() {
        Recipe spaghettiCarbonara = createRecipe("Spaghetti alla Carbonara", 
            "Classico primo piatto romano");
        spaghettiCarbonara.setPreparationTime(15);
        spaghettiCarbonara.setCookingTime(20);
        spaghettiCarbonara.setServings(4);
        spaghettiCarbonara.setDifficulty("Facile");
        spaghettiCarbonara.addIngredient("400g spaghetti");
        spaghettiCarbonara.addIngredient("200g guanciale");
        spaghettiCarbonara.addIngredient("4 uova");
        spaghettiCarbonara.addIngredient("100g pecorino romano");
        
        Preparation prep1 = createPreparation(1, "Tagliare il guanciale a cubetti", 5);
        Preparation prep2 = createPreparation(2, "Cuocere la pasta in acqua salata", 10);
        Preparation prep3 = createPreparation(3, "Rosolare il guanciale", 5);
        Preparation prep4 = createPreparation(4, "Mescolare uova e pecorino", 3);
        Preparation prep5 = createPreparation(5, "Mantecare pasta con uova e guanciale", 2);
        
        spaghettiCarbonara.addPreparation(prep1);
        spaghettiCarbonara.addPreparation(prep2);
        spaghettiCarbonara.addPreparation(prep3);
        spaghettiCarbonara.addPreparation(prep4);
        spaghettiCarbonara.addPreparation(prep5);
    }
    
    // ========== GESTIONE RICETTE ==========
    public Recipe createRecipe(String name, String description) {
        Recipe recipe = new Recipe(name, description);
        recipe.setId(nextRecipeId++);
        recipes.add(recipe);
        return recipe;
    }
    
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }
    
    public Recipe getRecipeById(int id) {
        return recipes.stream()
                .filter(recipe -> recipe.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean updateRecipe(Recipe recipe) {
        Recipe existingRecipe = getRecipeById(recipe.getId());
        if (existingRecipe != null) {
            existingRecipe.setName(recipe.getName());
            existingRecipe.setDescription(recipe.getDescription());
            existingRecipe.setPreparationTime(recipe.getPreparationTime());
            existingRecipe.setCookingTime(recipe.getCookingTime());
            existingRecipe.setServings(recipe.getServings());
            existingRecipe.setDifficulty(recipe.getDifficulty());
            existingRecipe.setIngredients(recipe.getIngredients());
            existingRecipe.setPreparations(recipe.getPreparations());
            return true;
        }
        return false;
    }
    
    public boolean deleteRecipe(int id) {
        return recipes.removeIf(recipe -> recipe.getId() == id);
    }
    
    // ========== GESTIONE PREPARAZIONI ==========
    public Preparation createPreparation(int stepNumber, String description, int timeInMinutes) {
        Preparation preparation = new Preparation(stepNumber, description, timeInMinutes);
        preparation.setId(nextPreparationId++);
        return preparation;
    }
    
    public void addPreparationToRecipe(int recipeId, Preparation preparation) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            recipe.addPreparation(preparation);
        }
    }
    
    public void removePreparationFromRecipe(int recipeId, int preparationId) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            recipe.getPreparations().removeIf(prep -> prep.getId() == preparationId);
        }
    }
    
    public void updatePreparationInRecipe(int recipeId, Preparation updatedPreparation) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            List<Preparation> preparations = recipe.getPreparations();
            for (int i = 0; i < preparations.size(); i++) {
                if (preparations.get(i).getId() == updatedPreparation.getId()) {
                    preparations.set(i, updatedPreparation);
                    break;
                }
            }
        }
    }
    
    // ========== METODI DI UTILITÀ ==========
    public List<Recipe> getRecipesByDifficulty(String difficulty) {
        return recipes.stream()
                .filter(recipe -> recipe.getDifficulty().equalsIgnoreCase(difficulty))
                .toList();
    }
    
    public List<Recipe> getRecipesByPreparationTime(int maxMinutes) {
        return recipes.stream()
                .filter(recipe -> recipe.getPreparationTime() <= maxMinutes)
                .toList();
    }
    
    public List<Recipe> getRecipesByServings(int servings) {
        return recipes.stream()
                .filter(recipe -> recipe.getServings() == servings)
                .toList();
    }
    
    public List<Recipe> searchRecipesByIngredient(String ingredient) {
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients().stream()
                        .anyMatch(ing -> ing.toLowerCase().contains(ingredient.toLowerCase())))
                .toList();
    }
    
    public int getTotalCookingTime(int recipeId) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            return recipe.getPreparationTime() + recipe.getCookingTime();
        }
        return 0;
    }
}