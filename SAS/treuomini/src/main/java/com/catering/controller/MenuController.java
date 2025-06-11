// MenuController.java - Gestione dei menu
package com.catering.controller;

import com.catering.model.Menu;
import com.catering.model.Section;
import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private List<Menu> menus;
    private int nextMenuId = 1;
    
    public MenuController() {
        this.menus = new ArrayList<>();
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
    
    // ========== GESTIONE SEZIONI NEI MENU ==========
    public void addSectionToMenu(int menuId, Section section) {
        Menu menu = getMenuById(menuId);
        if (menu != null && section != null) {
            menu.addSection(section);
        }
    }
    
    public void removeSectionFromMenu(int menuId, int sectionId) {
        Menu menu = getMenuById(menuId);
        if (menu != null) {
            menu.getSections().removeIf(section -> section.getId() == sectionId);
        }
    }
    
    public List<Section> getSectionsForMenu(int menuId) {
        Menu menu = getMenuById(menuId);
        return menu != null ? menu.getSections() : new ArrayList<>();
    }
    
    // ========== METODI DI UTILITÀ ==========
    public List<Menu> getMenusByPriceRange(double minPrice, double maxPrice) {
        return menus.stream()
                .filter(menu -> menu.getPrice() >= minPrice && menu.getPrice() <= maxPrice)
                .toList();
    }
    
    public double calculateTotalPrice(List<Integer> menuIds) {
        return menuIds.stream()
                .mapToDouble(id -> {
                    Menu menu = getMenuById(id);
                    return menu != null ? menu.getPrice() : 0.0;
                })
                .sum();
    }
}