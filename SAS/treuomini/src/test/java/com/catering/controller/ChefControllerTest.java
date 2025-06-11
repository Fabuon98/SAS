// ChefControllerTest.java - Test essenziali per il controller
// Posizione: src/test/java/com/catering/controller/ChefControllerTest.java

package com.catering.controller;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.catering.model.Event;
import com.catering.model.Menu;
import com.catering.model.Preparation;
import com.catering.model.Recipe;
import com.catering.model.Section;

class ChefControllerTest {
    
    private ChefController controller;
    
    @BeforeEach
    void setUp() {
        controller = new ChefController();
    }
    
    @Test
    void testCreateEvent() {
        // Given
        String name = "Matrimonio Test";
        LocalDate date = LocalDate.now().plusDays(30);
        String location = "Villa Roma";
        int guests = 100;
        
        // When
        Event event = controller.createEvent(name, date, location, guests);
        
        // Then
        assertNotNull(event);
        assertEquals(name, event.getName());
        assertEquals(date, event.getDate());
        assertEquals(location, event.getLocation());
        assertEquals(guests, event.getNumberOfGuests());
        assertTrue(event.getId() > 0);
    }
    
    @Test
    void testUpdateEvent() {
        // Given
        Event event = controller.createEvent("Evento Originale", LocalDate.now(), "Location Originale", 50);
        event.setName("Evento Modificato");
        event.setLocation("Nuova Location");
        event.setNumberOfGuests(75);
        
        // When
        boolean updated = controller.updateEvent(event);
        Event retrievedEvent = controller.getEventById(event.getId());
        
        // Then
        assertTrue(updated);
        assertEquals("Evento Modificato", retrievedEvent.getName());
        assertEquals("Nuova Location", retrievedEvent.getLocation());
        assertEquals(75, retrievedEvent.getNumberOfGuests());
    }
    
    @Test
    void testCreateRecipe() {
        // Given
        String name = "Spaghetti Carbonara";
        String description = "Ricetta tradizionale romana";
        
        // When
        Recipe recipe = controller.createRecipe(name, description);
        
        // Then
        assertNotNull(recipe);
        assertEquals(name, recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertTrue(recipe.getId() > 0);
        assertNotNull(recipe.getIngredients());
        assertNotNull(recipe.getPreparations());
    }
    
    @Test
    void testUpdateRecipe() {
        // Given
        Recipe recipe = controller.createRecipe("Ricetta Originale", "Descrizione originale");
        recipe.setName("Ricetta Modificata");
        recipe.setDescription("Nuova descrizione");
        recipe.setPreparationTime(30);
        recipe.setCookingTime(20);
        recipe.setServings(4);
        recipe.setDifficulty("Medio");
        
        // When
        boolean updated = controller.updateRecipe(recipe);
        Recipe retrievedRecipe = controller.getRecipeById(recipe.getId());
        
        // Then
        assertTrue(updated);
        assertEquals("Ricetta Modificata", retrievedRecipe.getName());
        assertEquals("Nuova descrizione", retrievedRecipe.getDescription());
        assertEquals(30, retrievedRecipe.getPreparationTime());
        assertEquals(20, retrievedRecipe.getCookingTime());
        assertEquals(4, retrievedRecipe.getServings());
        assertEquals("Medio", retrievedRecipe.getDifficulty());
    }
    
    @Test
    void testCreateMenu() {
        // Given
        String name = "Menu Degustazione";
        String description = "Menu per eventi speciali";
        double price = 75.50;
        
        // When
        Menu menu = controller.createMenu(name, description, price);
        
        // Then
        assertNotNull(menu);
        assertEquals(name, menu.getName());
        assertEquals(description, menu.getDescription());
        assertEquals(price, menu.getPrice());
        assertTrue(menu.getId() > 0);
    }
    
    @Test
    void testConnectMenuToEvent() {
        // Given
        Event event = controller.createEvent("Evento Test", LocalDate.now(), "Location", 50);
        Menu menu = controller.createMenu("Menu Test", "Descrizione", 60.0);
        
        // When
        controller.addMenuToEvent(event.getId(), menu.getId());
        Event retrievedEvent = controller.getEventById(event.getId());
        
        // Then
        assertEquals(1, retrievedEvent.getMenus().size());
        assertEquals(menu.getName(), retrievedEvent.getMenus().get(0).getName());
    }
    
    @Test
    void testConnectRecipeToSection() {
        // Given
        Section section = controller.createSection("Primi Piatti", "Pasta e risotti");
        Recipe recipe = controller.createRecipe("Pasta al Pomodoro", "Primo piatto semplice");
        
        // When
        controller.addRecipeToSection(section.getId(), recipe.getId());
        Section retrievedSection = controller.getSectionById(section.getId());
        
        // Then
        assertEquals(1, retrievedSection.getRecipes().size());
        assertEquals(recipe.getName(), retrievedSection.getRecipes().get(0).getName());
    }
    
    @Test
    void testAddIngredientsToRecipe() {
        // Given
        Recipe recipe = controller.createRecipe("Pasta Carbonara", "Ricetta romana");
        
        // When
        recipe.addIngredient("400g Spaghetti");
        recipe.addIngredient("200g Guanciale");
        recipe.addIngredient("4 Uova");
        
        // Then
        assertEquals(3, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains("400g Spaghetti"));
        assertTrue(recipe.getIngredients().contains("200g Guanciale"));
        assertTrue(recipe.getIngredients().contains("4 Uova"));
    }
    
    @Test
    void testAddPreparationToRecipe() {
        // Given
        Recipe recipe = controller.createRecipe("Test Recipe", "Descrizione test");
        Preparation preparation = controller.createPreparation(1, "Tagliare ingredienti", 10);
        
        // When
        recipe.addPreparation(preparation);
        
        // Then
        assertEquals(1, recipe.getPreparations().size());
        assertEquals(preparation, recipe.getPreparations().get(0));
        assertEquals("Tagliare ingredienti", recipe.getPreparations().get(0).getDescription());
        assertEquals(10, recipe.getPreparations().get(0).getTimeInMinutes());
    }
    
    @Test
    void testDeleteEvent() {
        // Given
        Event event = controller.createEvent("Evento da Eliminare", LocalDate.now(), "Location", 30);
        int eventId = event.getId();
        
        // When
        boolean deleted = controller.deleteEvent(eventId);
        Event retrievedEvent = controller.getEventById(eventId);
        
        // Then
        assertTrue(deleted);
        assertNull(retrievedEvent);
    }
}