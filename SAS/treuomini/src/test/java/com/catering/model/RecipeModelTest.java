// RecipeModelTest.java - Test per il modello Recipe
// Posizione: src/test/java/com/catering/model/RecipeModelTest.java

package com.catering.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeModelTest {
    
    private Recipe recipe;
    
    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }
    
    @Test
    void testCreateRecipeWithParameters() {
        // Given
        String name = "Tiramisu";
        String description = "Dolce italiano tradizionale";
        
        // When
        Recipe paramRecipe = new Recipe(name, description);
        
        // Then
        assertEquals(name, paramRecipe.getName());
        assertEquals(description, paramRecipe.getDescription());
        assertNotNull(paramRecipe.getIngredients());
        assertNotNull(paramRecipe.getPreparations());
        assertTrue(paramRecipe.getIngredients().isEmpty());
        assertTrue(paramRecipe.getPreparations().isEmpty());
    }
    
    @Test
    void testSetRecipeProperties() {
        // Given
        int prepTime = 45;
        int cookTime = 30;
        int servings = 6;
        String difficulty = "Difficile";
        
        // When
        recipe.setName("Lasagne");
        recipe.setDescription("Primo piatto al forno");
        recipe.setPreparationTime(prepTime);
        recipe.setCookingTime(cookTime);
        recipe.setServings(servings);
        recipe.setDifficulty(difficulty);
        
        // Then
        assertEquals("Lasagne", recipe.getName());
        assertEquals("Primo piatto al forno", recipe.getDescription());
        assertEquals(prepTime, recipe.getPreparationTime());
        assertEquals(cookTime, recipe.getCookingTime());
        assertEquals(servings, recipe.getServings());
        assertEquals(difficulty, recipe.getDifficulty());
    }
    
    @Test
    void testAddMultipleIngredients() {
        // Given
        String ingredient1 = "500g Mascarpone";
        String ingredient2 = "6 Uova";
        String ingredient3 = "200g Savoiardi";
        String ingredient4 = "Caffè espresso";
        
        // When
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        recipe.addIngredient(ingredient4);
        
        // Then
        assertEquals(4, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains(ingredient1));
        assertTrue(recipe.getIngredients().contains(ingredient2));
        assertTrue(recipe.getIngredients().contains(ingredient3));
        assertTrue(recipe.getIngredients().contains(ingredient4));
    }
    
    @Test
    void testAddMultiplePreparations() {
        // Given
        Preparation prep1 = new Preparation(1, "Separare tuorli da albumi", 5);
        Preparation prep2 = new Preparation(2, "Montare mascarpone con tuorli", 10);
        Preparation prep3 = new Preparation(3, "Preparare caffè e farlo raffreddare", 15);
        
        // When
        recipe.addPreparation(prep1);
        recipe.addPreparation(prep2);
        recipe.addPreparation(prep3);
        
        // Then
        assertEquals(3, recipe.getPreparations().size());
        assertEquals(prep1, recipe.getPreparations().get(0));
        assertEquals(prep2, recipe.getPreparations().get(1));
        assertEquals(prep3, recipe.getPreparations().get(2));
    }
    
    @Test
    void testRecipeToString() {
        // Given
        recipe.setName("Spaghetti Aglio e Olio");
        
        // When
        String result = recipe.toString();
        
        // Then
        assertEquals("Spaghetti Aglio e Olio", result);
    }
}