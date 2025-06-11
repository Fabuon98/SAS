// SectionController.java - Gestione delle sezioni
package com.catering.controller;

import com.catering.model.Section;
import com.catering.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class SectionController {
    private List<Section> sections;
    private int nextSectionId = 1;
    
    public SectionController() {
        this.sections = new ArrayList<>();
        initializeDefaultSections();
    }
    
    // Inizializza sezioni standard
    private void initializeDefaultSections() {
        createSection("Antipasti", "Primi piatti serviti per aprire il pasto");
        createSection("Primi Piatti", "Pasta, risotti e zuppe");
        createSection("Secondi Piatti", "Carne e pesce");
        createSection("Dolci", "Dessert e dolci");
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
    
    // ========== GESTIONE RICETTE NELLE SEZIONI ==========
    public void addRecipeToSection(int sectionId, Recipe recipe) {
        Section section = getSectionById(sectionId);
        if (section != null && recipe != null) {
            section.addRecipe(recipe);
        }
    }
    
    public void removeRecipeFromSection(int sectionId, int recipeId) {
        Section section = getSectionById(sectionId);
        if (section != null) {
            section.getRecipes().removeIf(recipe -> recipe.getId() == recipeId);
        }
    }
    
    public List<Recipe> getRecipesForSection(int sectionId) {
        Section section = getSectionById(sectionId);
        return section != null ? section.getRecipes() : new ArrayList<>();
    }
    
    // ========== METODI DI UTILITÀ ==========
    public List<Section> getSectionsByName(String namePattern) {
        return sections.stream()
                .filter(section -> section.getName().toLowerCase()
                        .contains(namePattern.toLowerCase()))
                .toList();
    }
    
    public int getTotalRecipesInSection(int sectionId) {
        Section section = getSectionById(sectionId);
        return section != null ? section.getRecipes().size() : 0;
    }
}