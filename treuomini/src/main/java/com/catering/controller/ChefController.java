// ChefController.java - Controller principale per gestire tutte le operazioni
package com.catering.controller;

import java.time.LocalDate;               // Importa tutte le classi del package model (Event, Menu, Section, Recipe, Preparation)
import java.util.ArrayList;               // Importa LocalDate per la gestione delle date
import java.util.List;               // Importa ArrayList per implementazioni di List

import com.catering.model.Event;                    // Importa List per l'uso di collezioni
import com.catering.model.Menu;
import com.catering.model.Preparation;
import com.catering.model.Recipe;
import com.catering.model.Section;

public class ChefController {
    // Collezioni in memoria per ciascun tipo di entità gestita
    private List<Event> events;            // Lista degli eventi
    private List<Menu> menus;              // Lista dei menu
    private List<Section> sections;        // Lista delle sezioni dei menu
    private List<Recipe> recipes;          // Lista delle ricette
    
    // Contatori per generare ID unici per ogni entità
    private int nextEventId = 1;
    private int nextMenuId = 1;
    private int nextSectionId = 1;
    private int nextRecipeId = 1;
    private int nextPreparationId = 1;
    
    public ChefController() {
        this.events = new ArrayList<>();   // Inizializza lista eventi
        this.menus = new ArrayList<>();    // Inizializza lista menu
        this.sections = new ArrayList<>(); // Inizializza lista sezioni
        this.recipes = new ArrayList<>();  // Inizializza lista ricette
        initializeDefaultData();           // Popola con dati di esempio
    }
    
    // Inizializza alcuni dati di esempio
    private void initializeDefaultData() {
        // Crea alcune sezioni standard
        createSection("Antipasti", "Primi piatti serviti per aprire il pasto");
        createSection("Primi Piatti", "Pasta, risotti e zuppe");
        createSection("Secondi Piatti", "Carne e pesce");
        createSection("Dolci", "Dessert e dolci");
        
        // Crea una ricetta di esempio: Spaghetti alla Carbonara
        Recipe spaghettiCarbonara = createRecipe(
            "Spaghetti alla Carbonara", 
            "Classico primo piatto romano"
        );
        // Imposta proprietà della ricetta
        spaghettiCarbonara.setPreparationTime(15);
        spaghettiCarbonara.setCookingTime(20);
        spaghettiCarbonara.setServings(4);
        spaghettiCarbonara.setDifficulty("Facile");
        // Aggiunge ingredienti
        spaghettiCarbonara.addIngredient("400g spaghetti");
        spaghettiCarbonara.addIngredient("200g guanciale");
        spaghettiCarbonara.addIngredient("4 uova");
        spaghettiCarbonara.addIngredient("100g pecorino romano");
        
        // Crea i singoli step di preparazione
        Preparation prep1 = new Preparation(1, "Tagliare il guanciale a cubetti", 5);
        Preparation prep2 = new Preparation(2, "Cuocere la pasta in acqua salata", 10);
        Preparation prep3 = new Preparation(3, "Rosolare il guanciale", 5);
        Preparation prep4 = new Preparation(4, "Mescolare uova e pecorino", 3);
        Preparation prep5 = new Preparation(5, "Mantecare pasta con uova e guanciale", 2);
        
        // Aggiunge gli step alla ricetta
        spaghettiCarbonara.addPreparation(prep1);
        spaghettiCarbonara.addPreparation(prep2);
        spaghettiCarbonara.addPreparation(prep3);
        spaghettiCarbonara.addPreparation(prep4);
        spaghettiCarbonara.addPreparation(prep5);
    }
    
    // ========== GESTIONE EVENTI ==========
    
    /** Crea un nuovo evento e gli assegna un ID univoco */
    public Event createEvent(String name, LocalDate date, String location, int numberOfGuests) {
        Event event = new Event(name, date, location, numberOfGuests);
        event.setId(nextEventId++);       // Assegna e incrementa l'ID
        events.add(event);                // Aggiunge alla lista in memoria
        return event;                     // Restituisce l'oggetto creato
    }
    
    /** Restituisce una copia della lista di tutti gli eventi */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    /** Cerca e restituisce l'evento con l'ID specificato, oppure null se non trovato */
    public Event getEventById(int id) {
        return events.stream()
                .filter(event -> event.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /** Aggiorna un evento esistente; ritorna true se l'aggiornamento ha avuto successo */
    public boolean updateEvent(Event event) {
        Event existingEvent = getEventById(event.getId());
        if (existingEvent != null) {
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            existingEvent.setLocation(event.getLocation());
            existingEvent.setNumberOfGuests(event.getNumberOfGuests());
            return true;
        }
        return false;
    }
    
    /** Rimuove l'evento con l'ID specificato; ritorna true se la rimozione ha avuto successo */
    public boolean deleteEvent(int id) {
        return events.removeIf(event -> event.getId() == id);
    }
    
    // ========== GESTIONE MENU ==========
    
    /** Crea un nuovo menu e gli assegna un ID univoco */
    public Menu createMenu(String name, String description, double price) {
        Menu menu = new Menu(name, description, price);
        menu.setId(nextMenuId++);
        menus.add(menu);
        return menu;
    }
    
    /** Restituisce una copia della lista di tutti i menu */
    public List<Menu> getAllMenus() {
        return new ArrayList<>(menus);
    }
    
    /** Cerca e restituisce il menu con l'ID specificato, oppure null se non trovato */
    public Menu getMenuById(int id) {
        return menus.stream()
                .filter(menu -> menu.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /** Aggiorna un menu esistente; ritorna true se la modifica ha avuto successo */
    public boolean updateMenu(Menu menu) {
        Menu existingMenu = getMenuById(menu.getId());
        if (existingMenu != null) {
            existingMenu.setName(menu.getName());
            existingMenu.setDescription(menu.getDescription());
            existingMenu.setPrice(menu.getPrice());
            return true;
        }
        return false;
    }
    
    /** Rimuove il menu con l'ID specificato; ritorna true se la rimozione ha avuto successo */
    public boolean deleteMenu(int id) {
        return menus.removeIf(menu -> menu.getId() == id);
    }
    
    // ========== GESTIONE SEZIONI ==========
    
    /** Crea una nuova sezione del menu e gli assegna un ID univoco */
    public Section createSection(String name, String description) {
        Section section = new Section(name, description);
        section.setId(nextSectionId++);
        sections.add(section);
        return section;
    }
    
    /** Restituisce una copia della lista di tutte le sezioni */
    public List<Section> getAllSections() {
        return new ArrayList<>(sections);
    }
    
    /** Cerca e restituisce la sezione con l'ID specificato, oppure null se non trovata */
    public Section getSectionById(int id) {
        return sections.stream()
                .filter(section -> section.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /** Aggiorna una sezione esistente; ritorna true se la modifica ha avuto successo */
    public boolean updateSection(Section section) {
        Section existingSection = getSectionById(section.getId());
        if (existingSection != null) {
            existingSection.setName(section.getName());
            existingSection.setDescription(section.getDescription());
            return true;
        }
        return false;
    }
    
    /** Rimuove la sezione con l'ID specificato; ritorna true se la rimozione ha avuto successo */
    public boolean deleteSection(int id) {
        return sections.removeIf(section -> section.getId() == id);
    }
    
    // ========== GESTIONE RICETTE ==========
    
    /** Crea una nuova ricetta e le assegna un ID univoco */
    public Recipe createRecipe(String name, String description) {
        Recipe recipe = new Recipe(name, description);
        recipe.setId(nextRecipeId++);
        recipes.add(recipe);
        return recipe;
    }
    
    /** Restituisce una copia della lista di tutte le ricette */
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }
    
    /** Cerca e restituisce la ricetta con l'ID specificato, oppure null se non trovata */
    public Recipe getRecipeById(int id) {
        return recipes.stream()
                .filter(recipe -> recipe.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /** Aggiorna una ricetta esistente; ritorna true se la modifica ha avuto successo */
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
    
    /** Rimuove la ricetta con l'ID specificato; ritorna true se la rimozione ha avuto successo */
    public boolean deleteRecipe(int id) {
        return recipes.removeIf(recipe -> recipe.getId() == id);
    }
    
    // ========== GESTIONE PREPARAZIONI ==========
    
    /** Crea un oggetto Preparation (step di ricetta) e gli assegna un ID univoco */
    public Preparation createPreparation(int stepNumber, String description, int timeInMinutes) {
        Preparation preparation = new Preparation(stepNumber, description, timeInMinutes);
        preparation.setId(nextPreparationId++);
        return preparation;                // Nota: non viene aggiunta direttamente a nessuna lista centrale
    }
    
    /** Aggiunge uno step di preparazione a una ricetta esistente */
    public void addPreparationToRecipe(int recipeId, Preparation preparation) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            recipe.addPreparation(preparation);
        }
    }
    
    /** Rimuove uno step di preparazione da una ricetta esistente */
    public void removePreparationFromRecipe(int recipeId, int preparationId) {
        Recipe recipe = getRecipeById(recipeId);
        if (recipe != null) {
            recipe.getPreparations().removeIf(prep -> prep.getId() == preparationId);
        }
    }
    
    // ========== METODI DI COLLEGAMENTO TRA ENTITÀ ==========
    
    /** Associa un menu a un evento */
    public void addMenuToEvent(int eventId, int menuId) {
        Event event = getEventById(eventId);
        Menu menu = getMenuById(menuId);
        if (event != null && menu != null) {
            event.addMenu(menu);
        }
    }
    
    /** Associa una sezione a un menu */
    public void addSectionToMenu(int menuId, int sectionId) {
        Menu menu = getMenuById(menuId);
        Section section = getSectionById(sectionId);
        if (menu != null && section != null) {
            menu.addSection(section);
        }
    }
    
    /** Associa una ricetta a una sezione */
    public void addRecipeToSection(int sectionId, int recipeId) {
        Section section = getSectionById(sectionId);
        Recipe recipe = getRecipeById(recipeId);
        if (section != null && recipe != null) {
            section.addRecipe(recipe);
        }
    }
}
