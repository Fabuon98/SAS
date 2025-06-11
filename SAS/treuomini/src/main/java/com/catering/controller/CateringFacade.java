// CateringFacade.java - Facade per coordinare tutti i controller
package com.catering.controller;

import com.catering.model.*;
import java.time.LocalDate;
import java.util.List;

public class CateringFacade {
    private EventController eventController;
    private MenuController menuController;
    private SectionController sectionController;
    private RecipeController recipeController;
    
    public CateringFacade() {
        this.eventController = new EventController();
        this.menuController = new MenuController();
        this.sectionController = new SectionController();
        this.recipeController = new RecipeController();
    }
    
    // ========== OPERAZIONI SU EVENTI ==========
    public Event createEvent(String name, LocalDate date, String location, int numberOfGuests) {
        return eventController.createEvent(name, date, location, numberOfGuests);
    }
    
    public List<Event> getAllEvents() {
        return eventController.getAllEvents();
    }
    
    public Event getEventById(int id) {
        return eventController.getEventById(id);
    }
    
    // ========== OPERAZIONI SU MENU ==========
    public Menu createMenu(String name, String description, double price) {
        return menuController.createMenu(name, description, price);
    }
    
    public List<Menu> getAllMenus() {
        return menuController.getAllMenus();
    }
    
    public Menu getMenuById(int id) {
        return menuController.getMenuById(id);
    }
    
    // ========== OPERAZIONI SU SEZIONI ==========
    public Section createSection(String name, String description) {
        return sectionController.createSection(name, description);
    }
    
    public List<Section> getAllSections() {
        return sectionController.getAllSections();
    }
    
    public Section getSectionById(int id) {
        return sectionController.getSectionById(id);
    }
    
    // ========== OPERAZIONI SU RICETTE ==========
    public Recipe createRecipe(String name, String description) {
        return recipeController.createRecipe(name, description);
    }
    
    public List<Recipe> getAllRecipes() {
        return recipeController.getAllRecipes();
    }
    
    public Recipe getRecipeById(int id) {
        return recipeController.getRecipeById(id);
    }
    
    // ========== OPERAZIONI COMPOSITE ==========
    public void createCompleteMenu(String menuName, String menuDescription, double price,
                                 int eventId, List<Integer> sectionIds) {
        // Crea il menu
        Menu menu = menuController.createMenu(menuName, menuDescription, price);
        
        // Aggiunge il menu all'evento
        eventController.addMenuToEvent(eventId, menu);
        
        // Aggiunge le sezioni al menu
        for (int sectionId : sectionIds) {
            Section section = sectionController.getSectionById(sectionId);
            if (section != null) {
                menuController.addSectionToMenu(menu.getId(), section);
            }
        }
    }
    
    public void addRecipeToMenuSection(int menuId, int sectionId, int recipeId) {
        Menu menu = menuController.getMenuById(menuId);
        Section section = sectionController.getSectionById(sectionId);
        Recipe recipe = recipeController.getRecipeById(recipeId);
        
        if (menu != null && section != null && recipe != null) {
            // Verifica che la sezione appartenga al menu
            if (menu.getSections().contains(section)) {
                sectionController.addRecipeToSection(sectionId, recipe);
            }
        }
    }
    
    public EventSummary getEventSummary(int eventId) {
        Event event = eventController.getEventById(eventId);
        if (event == null) return null;
        
        EventSummary summary = new EventSummary();
        summary.setEvent(event);
        
        double totalPrice = 0;
        int totalRecipes = 0;
        
        for (Menu menu : event.getMenus()) {
            totalPrice += menu.getPrice();
            for (Section section : menu.getSections()) {
                totalRecipes += section.getRecipes().size();
            }
        }
        
        summary.setTotalMenuPrice(totalPrice);
        summary.setTotalRecipes(totalRecipes);
        
        return summary;
    }
    
    // ========== GETTER PER I CONTROLLER ==========
    public EventController getEventController() {
        return eventController;
    }
    
    public MenuController getMenuController() {
        return menuController;
    }
    
    public SectionController getSectionController() {
        return sectionController;
    }
    
    public RecipeController getRecipeController() {
        return recipeController;
    }
    
    // Classe interna per il riepilogo eventi
    public static class EventSummary {
        private Event event;
        private double totalMenuPrice;
        private int totalRecipes;
        
        // Getters e setters
        public Event getEvent() { return event; }
        public void setEvent(Event event) { this.event = event; }
        
        public double getTotalMenuPrice() { return totalMenuPrice; }
        public void setTotalMenuPrice(double totalMenuPrice) { this.totalMenuPrice = totalMenuPrice; }
        
        public int getTotalRecipes() { return totalRecipes; }
        public void setTotalRecipes(int totalRecipes) { this.totalRecipes = totalRecipes; }
    }
}