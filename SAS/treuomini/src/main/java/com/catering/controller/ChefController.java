// ChefController.java - Controller principale per gestire tutte le operazioni
package com.catering.controller;

import com.catering.model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChefController {
    private List<Event> events;
    private List<Menu> menus;
    private List<Section> sections;
    private List<Recipe> recipes;
    private int nextEventId = 1;
    private int nextMenuId = 1;
    private int nextSectionId = 1;
    private int nextRecipeId = 1;
    private int nextPreparationId = 1;
    
    public ChefController() {
        this.events = new ArrayList<>();
        this.menus = new ArrayList<>();
        this.sections = new ArrayList<>();
        this.recipes = new ArrayList<>();
        initializeDefaultData();
    }
    
    // Inizializza alcuni dati di esempio
    private void initializeDefaultData() {
        // Crea alcune sezioni standard
        createSection("Antipasti", "Primi piatti serviti per aprire il pasto");
        createSection("Primi Piatti", "Pasta, risotti e zuppe");
        createSection("Secondi Piatti", "Carne e pesce");
        createSection("Dolci", "Dessert e dolci");
        
        // Crea alcune ricette di esempio
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
        
        Preparation prep1 = new Preparation(1, "Tagliare il guanciale a cubetti", 5);
        Preparation prep2 = new Preparation(2, "Cuocere la pasta in acqua salata", 10);
        Preparation prep3 = new Preparation(3, "Rosolare il guanciale", 5);
        Preparation prep4 = new Preparation(4, "Mescolare uova e pecorino", 3);
        Preparation prep5 = new Preparation(5, "Mantecare pasta con uova e guanciale", 2);
        
        spaghettiCarbonara.addPreparation(prep1);
        spaghettiCarbonara.addPreparation(prep2);
        spaghettiCarbonara.addPreparation(prep3);
        spaghettiCarbonara.addPreparation(prep4);
        spaghettiCarbonara.addPreparation(prep5);
    }
    
    // ========== GESTIONE EVENTI ==========
    public Event createEvent(String name, LocalDate date, String location, int numberOfGuests) {
        Event event = new Event(name, date, location, numberOfGuests);
        event.setId(nextEventId++);
        events.add(event);
        return event;
    }
    
    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }
    
    public Event getEventById(int id) {
        return events.stream()
                .filter(event -> event.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
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
    
    public boolean deleteEvent(int id) {
        return events.removeIf(event -> event.getId() == id);
    }
    
    // ========== GESTIONE MENU ==========
    public Menu createMenu(String name, String description, double price) {
        Menu menu = new Menu(name, description, price);
        menu.setId(nextMenuId++);
        menus.add(menu);
        return menu;
    }
    
    public List<Menu> getAllMenus() {
        return new ArrayList<>(menus);
    }
    
    public Menu getMenuById(int id) {
        return menus.stream()
                .filter(menu -> menu.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
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
    
    public boolean deleteMenu(int id) {
        return menus.removeIf(menu -> menu.getId() == id);
    }
    
    // ========== GESTIONE SEZIONI ==========
    public Section createSection(String name, String description) {
        Section section = new Section(name, description);
        section.setId(nextSectionId++);
        sections.add(section);
        return section;
    }
    
    public List<Section> getAllSections() {
        return new ArrayList<>(sections);
    }
    
    public Section getSectionById(int id) {
        return sections.stream()
                .filter(section -> section.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean updateSection(Section section) {
        Section existingSection = getSectionById(section.getId());
        if (existingSection != null) {
            existingSection.setName(section.getName());
            existingSection.setDescription(section.getDescription());
            return true;
        }
        return false;
    }
    
    public boolean deleteSection(int id) {
        return sections.removeIf(section -> section.getId() == id);
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
    
    // ========== METODI DI COLLEGAMENTO ==========
    public void addMenuToEvent(int eventId, int menuId) {
        Event event = getEventById(eventId);
        Menu menu = getMenuById(menuId);
        if (event != null && menu != null) {
            event.addMenu(menu);
        }
    }
    
    public void addSectionToMenu(int menuId, int sectionId) {
        Menu menu = getMenuById(menuId);
        Section section = getSectionById(sectionId);
        if (menu != null && section != null) {
            menu.addSection(section);
        }
    }
    
    public void addRecipeToSection(int sectionId, int recipeId) {
        Section section = getSectionById(sectionId);
        Recipe recipe = getRecipeById(recipeId);
        if (section != null && recipe != null) {
            section.addRecipe(recipe);
        }
    }
}