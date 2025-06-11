// ChefControllerTest.java - Test essenziali per il controller ChefController
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

/**
 * Classe di test per ChefController.
 * Contiene unit test per verificare le operazioni CRUD e le associazioni
 * tra eventi, menu, ricette e sezioni.
 */
class ChefControllerTest {
    
    // Istanza del controller da testare
    private ChefController controller;
    
    /**
     * Inizializza una nuova istanza di controller prima di ogni test.
     * Garantisce un ambiente pulito e indipendente.
     */
    @BeforeEach
    void setUp() {
        controller = new ChefController();
    }
    
    /**
     * Test del metodo createEvent:
     * - Dovrebbe restituire un oggetto Event non null
     * - I campi nome, data, location e numero di ospiti devono corrispondere a quelli passati
     * - L'id generato deve essere maggiore di zero (indica persistenza simulata)
     */
    @Test
    void testCreateEvent() {
        // Given: dati di input per l'evento
        String name     = "Matrimonio Test";
        LocalDate date  = LocalDate.now().plusDays(30);
        String location = "Villa Roma";
        int guests      = 100;
        
        // When: creo l'evento tramite il controller
        Event event = controller.createEvent(name, date, location, guests);
        
        // Then: verifiche di base sul risultato
        assertNotNull(event, "L'evento creato non deve essere null");
        assertEquals(name,     event.getName(),             "Nome evento errato");
        assertEquals(date,     event.getDate(),             "Data evento errata");
        assertEquals(location, event.getLocation(),         "Location evento errata");
        assertEquals(guests,   event.getNumberOfGuests(),   "Numero di ospiti errato");
        assertTrue(event.getId() > 0, "L'ID deve essere maggiore di zero");
    }
    
    /**
     * Test del metodo updateEvent:
     * - Crea un evento, ne modifica i campi e chiama updateEvent()
     * - Verifica che updateEvent restituisca true (successo)
     * - Recupera nuovamente l'evento per controllare che le modifiche siano effettive
     */
    @Test
    void testUpdateEvent() {
        // Given: creo prima un evento originale
        Event event = controller.createEvent("Evento Originale", LocalDate.now(), "Location Originale", 50);
        // Modifico alcuni campi dell'oggetto ottenuto
        event.setName("Evento Modificato");
        event.setLocation("Nuova Location");
        event.setNumberOfGuests(75);
        
        // When: tento di aggiornare l'evento nel controller
        boolean updated      = controller.updateEvent(event);
        Event retrievedEvent = controller.getEventById(event.getId());
        
        // Then: l'update deve aver avuto successo e le proprietà devono essere cambiate
        assertTrue(updated, "L'updateEvent dovrebbe ritornare true");
        assertEquals("Evento Modificato", retrievedEvent.getName());
        assertEquals("Nuova Location",    retrievedEvent.getLocation());
        assertEquals(75,                  retrievedEvent.getNumberOfGuests());
    }
    
    /**
     * Test del metodo createRecipe:
     * - Verifica creazione di una ricetta con nome e descrizione
     * - Controlla che id, liste di ingredienti e preparazioni siano inizializzati
     */
    @Test
    void testCreateRecipe() {
        // Given: parametri per la nuova ricetta
        String name        = "Spaghetti Carbonara";
        String description = "Ricetta tradizionale romana";
        
        // When: creo la ricetta tramite il controller
        Recipe recipe = controller.createRecipe(name, description);
        
        // Then: verifiche di base
        assertNotNull(recipe, "La ricetta creata non deve essere null");
        assertEquals(name,        recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertTrue(recipe.getId() > 0, "L'ID della ricetta deve essere > 0");
        assertNotNull(recipe.getIngredients(),  "Lista ingredienti non inizializzata");
        assertNotNull(recipe.getPreparations(), "Lista preparazioni non inizializzata");
    }
    
    /**
     * Test del metodo updateRecipe:
     * - Crea una ricetta, ne modifica proprietà multiple
     * - Chiama updateRecipe(), quindi recupera la ricetta per validare i cambiamenti
     */
    @Test
    void testUpdateRecipe() {
        // Given: ricetta iniziale
        Recipe recipe = controller.createRecipe("Ricetta Originale", "Descrizione originale");
        // Imposto nuovi valori
        recipe.setName("Ricetta Modificata");
        recipe.setDescription("Nuova descrizione");
        recipe.setPreparationTime(30);
        recipe.setCookingTime(20);
        recipe.setServings(4);
        recipe.setDifficulty("Medio");
        
        // When: update tramite controller
        boolean updated         = controller.updateRecipe(recipe);
        Recipe retrievedRecipe  = controller.getRecipeById(recipe.getId());
        
        // Then: verifiche sulle nuove proprietà
        assertTrue(updated, "L'updateRecipe dovrebbe ritornare true");
        assertEquals("Ricetta Modificata",    retrievedRecipe.getName());
        assertEquals("Nuova descrizione",     retrievedRecipe.getDescription());
        assertEquals(30,                      retrievedRecipe.getPreparationTime());
        assertEquals(20,                      retrievedRecipe.getCookingTime());
        assertEquals(4,                       retrievedRecipe.getServings());
        assertEquals("Medio",                 retrievedRecipe.getDifficulty());
    }
    
    /**
     * Test del metodo createMenu:
     * - Crea un menu con nome, descrizione e prezzo
     * - Verifica corretto popolamento dei campi e generazione dell'id
     */
    @Test
    void testCreateMenu() {
        // Given: dati per il nuovo menu
        String name        = "Menu Degustazione";
        String description = "Menu per eventi speciali";
        double price       = 75.50;
        
        // When: creo il menu
        Menu menu = controller.createMenu(name, description, price);
        
        // Then: controlli di base
        assertNotNull(menu, "Il menu creato non deve essere null");
        assertEquals(name,        menu.getName());
        assertEquals(description, menu.getDescription());
        assertEquals(price,       menu.getPrice());
        assertTrue(menu.getId() > 0, "L'ID del menu deve essere > 0");
    }
    
    /**
     * Test del metodo addMenuToEvent:
     * - Collega un menu creato a un evento esistente
     * - Recupera l'evento e verifica che nella sua lista di menu compaia quello collegato
     */
    @Test
    void testConnectMenuToEvent() {
        // Given: creo evento e menu
        Event event = controller.createEvent("Evento Test", LocalDate.now(), "Location", 50);
        Menu menu   = controller.createMenu("Menu Test", "Descrizione", 60.0);
        
        // When: associo il menu all'evento
        controller.addMenuToEvent(event.getId(), menu.getId());
        Event retrievedEvent = controller.getEventById(event.getId());
        
        // Then: l'evento deve avere un solo menu e con lo stesso nome
        assertEquals(1, retrievedEvent.getMenus().size());
        assertEquals(menu.getName(), retrievedEvent.getMenus().get(0).getName());
    }
    
    /**
     * Test del metodo addRecipeToSection:
     * - Crea una sezione e una ricetta
     * - Collega la ricetta alla sezione
     * - Recupera la sezione e verifica la presenza della ricetta nella lista
     */
    @Test
    void testConnectRecipeToSection() {
        // Given: creo sezione e ricetta
        Section section = controller.createSection("Primi Piatti", "Pasta e risotti");
        Recipe recipe   = controller.createRecipe("Pasta al Pomodoro", "Primo piatto semplice");
        
        // When: associo la ricetta alla sezione
        controller.addRecipeToSection(section.getId(), recipe.getId());
        Section retrievedSection = controller.getSectionById(section.getId());
        
        // Then: la sezione deve contenere esattamente una ricetta con lo stesso nome
        assertEquals(1, retrievedSection.getRecipes().size());
        assertEquals(recipe.getName(), retrievedSection.getRecipes().get(0).getName());
    }
    
    /**
     * Test dell'aggiunta di ingredienti direttamente sulla Recipe:
     * - Controlla che addIngredient modifichi la lista interna
     * - Verifica dimensione e contenuto della lista
     */
    @Test
    void testAddIngredientsToRecipe() {
        // Given: ricetta vuota
        Recipe recipe = controller.createRecipe("Pasta Carbonara", "Ricetta romana");
        
        // When: aggiungo tre ingredienti
        recipe.addIngredient("400g Spaghetti");
        recipe.addIngredient("200g Guanciale");
        recipe.addIngredient("4 Uova");
        
        // Then: lista ingredienti deve contenere i tre elementi
        assertEquals(3, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains("400g Spaghetti"));
        assertTrue(recipe.getIngredients().contains("200g Guanciale"));
        assertTrue(recipe.getIngredients().contains("4 Uova"));
    }
    
    /**
     * Test dell'aggiunta di una preparazione tramite controller:
     * - Verifica che createPreparation restituisca un oggetto valido
     * - Aggiunge la preparazione alla ricetta e controlla che sia correttamente inserita
     */
    @Test
    void testAddPreparationToRecipe() {
        // Given: ricetta e creazione di un oggetto Preparation tramite controller
        Recipe recipe       = controller.createRecipe("Test Recipe", "Descrizione test");
        Preparation preparation = controller.createPreparation(1, "Tagliare ingredienti", 10);
        
        // When: aggiungo la preparazione
        recipe.addPreparation(preparation);
        
        // Then: la lista di preparazioni deve contenere esattamente quell'oggetto
        assertEquals(1, recipe.getPreparations().size());
        assertEquals(preparation, recipe.getPreparations().get(0));
        assertEquals("Tagliare ingredienti", recipe.getPreparations().get(0).getDescription());
        assertEquals(10, recipe.getPreparations().get(0).getTimeInMinutes());
    }
    
    /**
     * Test del metodo deleteEvent:
     * - Crea un evento, ne salva l'id, lo elimina
     * - Verifica che deleteEvent ritorni true
     * - Al recupero, l'evento deve risultare null (non più esistente)
     */
    @Test
    void testDeleteEvent() {
        // Given: creo e salvo l'ID di un evento
        Event event   = controller.createEvent("Evento da Eliminare", LocalDate.now(), "Location", 30);
        int eventId   = event.getId();
        
        // When: elimino l'evento tramite controller
        boolean deleted      = controller.deleteEvent(eventId);
        Event retrievedEvent = controller.getEventById(eventId);
        
        // Then: l'operazione di delete deve restituire true e il recupero deve dare null
        assertTrue(deleted, "deleteEvent dovrebbe ritornare true");
        assertNull(retrievedEvent, "L'evento non dovrebbe più esistere");
    }
}