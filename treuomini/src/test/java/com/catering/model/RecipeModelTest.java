// RecipeModelTest.java - Test per la classe modello Recipe
// Posizione: src/test/java/com/catering/model/RecipeModelTest.java

package com.catering.model;

// Import delle asserzioni di JUnit Jupiter per verificare i risultati attesi
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe di test per il modello Recipe.
 * Verifica i costruttori, i metodi di accesso (get/set) e le funzioni di gestione
 * di ingredienti e preparazioni.
 */
class RecipeModelTest {
    
    // Istanza di Recipe che verrà inizializzata prima di ogni test
    private Recipe recipe;
    
    /**
     * Metodo eseguito prima di ciascun test.
     * Inizializza una nuova istanza di Recipe usando il costruttore vuoto.
     */
    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }
    
    /**
     * Verifica il costruttore che accetta nome e descrizione.
     * Controlla che:
     * - name e description vengano impostati correttamente
     * - le liste di ingredients e preparations siano inizializzate non-nulle e vuote
     */
    @Test
    void testCreateRecipeWithParameters() {
        // Given: parametri di creazione
        String name = "Tiramisu";
        String description = "Dolce italiano tradizionale";
        
        // When: creo una nuova Recipe con nome e descrizione
        Recipe paramRecipe = new Recipe(name, description);
        
        // Then: verifico che i campi e le collection siano corretti
        assertEquals(name, paramRecipe.getName());
        assertEquals(description, paramRecipe.getDescription());
        assertNotNull(paramRecipe.getIngredients(), "La lista ingredienti non deve essere null");
        assertNotNull(paramRecipe.getPreparations(), "La lista preparazioni non deve essere null");
        assertTrue(paramRecipe.getIngredients().isEmpty(), "La lista ingredienti deve essere vuota all'inizio");
        assertTrue(paramRecipe.getPreparations().isEmpty(), "La lista preparazioni deve essere vuota all'inizio");
    }
    
    /**
     * Testa i metodi setter e getter per i campi scalar:
     * - preparationTime
     * - cookingTime
     * - servings
     * - difficulty
     * Inoltre verifica set/get di name e description.
     */
    @Test
    void testSetRecipeProperties() {
        // Given: valori di prova
        int prepTime   = 45;
        int cookTime   = 30;
        int servings   = 6;
        String difficulty = "Difficile";
        
        // When: imposto tutti i campi su recipe
        recipe.setName("Lasagne");
        recipe.setDescription("Primo piatto al forno");
        recipe.setPreparationTime(prepTime);
        recipe.setCookingTime(cookTime);
        recipe.setServings(servings);
        recipe.setDifficulty(difficulty);
        
        // Then: verifico che i getter restituiscano i valori impostati
        assertEquals("Lasagne", recipe.getName());
        assertEquals("Primo piatto al forno", recipe.getDescription());
        assertEquals(prepTime, recipe.getPreparationTime());
        assertEquals(cookTime, recipe.getCookingTime());
        assertEquals(servings, recipe.getServings());
        assertEquals(difficulty, recipe.getDifficulty());
    }
    
    /**
     * Verifica l'aggiunta di più ingredienti:
     * - addIngredient deve aumentare la dimensione della lista
     * - gli ingredienti aggiunti devono essere presenti nella lista
     */
    @Test
    void testAddMultipleIngredients() {
        // Given: quattro stringhe di ingredienti
        String ingredient1 = "500g Mascarpone";
        String ingredient2 = "6 Uova";
        String ingredient3 = "200g Savoiardi";
        String ingredient4 = "Caffè espresso";
        
        // When: aggiungo ciascun ingrediente
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        recipe.addIngredient(ingredient4);
        
        // Then: verifico la dimensione e la presenza di tutti gli elementi
        assertEquals(4, recipe.getIngredients().size(), "La lista dovrebbe contenere 4 elementi");
        assertTrue(recipe.getIngredients().contains(ingredient1));
        assertTrue(recipe.getIngredients().contains(ingredient2));
        assertTrue(recipe.getIngredients().contains(ingredient3));
        assertTrue(recipe.getIngredients().contains(ingredient4));
    }
    
    /**
     * Verifica l'aggiunta di più oggetti Preparation:
     * - addPreparation deve rispettare l'ordine di inserimento
     * - le preparazioni devono comparire nella lista nello stesso ordine
     */
    @Test
    void testAddMultiplePreparations() {
        // Given: tre istanze di Preparation con ordine e descrizione diverse
        Preparation prep1 = new Preparation(1, "Separare tuorli da albumi", 5);
        Preparation prep2 = new Preparation(2, "Montare mascarpone con tuorli", 10);
        Preparation prep3 = new Preparation(3, "Preparare caffè e farlo raffreddare", 15);
        
        // When: aggiungo le tre preparazioni
        recipe.addPreparation(prep1);
        recipe.addPreparation(prep2);
        recipe.addPreparation(prep3);
        
        // Then: verifico dimensione e corrispondenza degli oggetti per indice
        assertEquals(3, recipe.getPreparations().size(), "Dovrebbero esserci 3 preparazioni");
        assertEquals(prep1, recipe.getPreparations().get(0));
        assertEquals(prep2, recipe.getPreparations().get(1));
        assertEquals(prep3, recipe.getPreparations().get(2));
    }
    
    /**
     * Test del metodo toString() di Recipe:
     * Deve restituire semplicemente il nome della ricetta.
     */
    @Test
    void testRecipeToString() {
        // Given: imposto il nome su recipe
        recipe.setName("Spaghetti Aglio e Olio");
        
        // When: invoco toString()
        String result = recipe.toString();
        
        // Then: il risultato deve coincidere con il nome
        assertEquals("Spaghetti Aglio e Olio", result);
    }
}
