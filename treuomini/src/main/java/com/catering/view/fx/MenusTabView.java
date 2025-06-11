// MenusTabView.java - Gestione della tab Menu
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Menu;
import com.catering.model.Section;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MenusTabView {
    private ChefController controller;
    private ListView<Menu> menuListView;
    
    public MenusTabView(ChefController controller) {
        this.controller = controller;
        this.menuListView = new ListView<>();
    }
    
    public Tab createTab() {
        Tab tab = new Tab("Menu");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }
    
    private VBox createContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        menuListView.setPrefHeight(300);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addMenuBtn = new Button("Aggiungi Menu");
        Button editMenuBtn = new Button("Modifica Menu");
        Button deleteMenuBtn = new Button("Elimina Menu");
        Button viewMenuBtn = new Button("Visualizza Menu");
        
        buttons.getChildren().addAll(addMenuBtn, editMenuBtn, deleteMenuBtn, viewMenuBtn);
        
        addMenuBtn.setOnAction(e -> showAddMenuDialog());
        editMenuBtn.setOnAction(e -> showEditMenuDialog());
        deleteMenuBtn.setOnAction(e -> deleteSelectedMenu());
        viewMenuBtn.setOnAction(e -> showMenuDetails());
        
        content.getChildren().addAll(title, menuListView, buttons);
        return content;
    }
    
    private void showAddMenuDialog() {
        MenuDialog dialog = new MenuDialog(controller, null);
        dialog.showAndWait().ifPresent(menu -> {
            refreshData();
            AlertUtils.showSuccess("Menu creato con successo!");
        });
    }
    
    private void showEditMenuDialog() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da modificare.");
            return;
        }
        
        MenuDialog dialog = new MenuDialog(controller, selected);
        dialog.showAndWait().ifPresent(menu -> {
            refreshData();
            AlertUtils.showSuccess("Menu modificato con successo!");
        });
    }
    
    private void deleteSelectedMenu() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da eliminare.");
            return;
        }
        
        if (AlertUtils.confirmDelete("menu", selected.getName())) {
            controller.deleteMenu(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Menu eliminato con successo!");
        }
    }
    
    private void showMenuDetails() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da visualizzare.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        content.append("Descrizione: ").append(selected.getDescription()).append("\n");
        content.append("Prezzo: €").append(selected.getPrice()).append("\n");
        content.append("Sezioni: ").append(selected.getSections().size()).append("\n\n");
        
        if (!selected.getSections().isEmpty()) {
            content.append("Lista Sezioni:\n");
            for (Section section : selected.getSections()) {
                content.append("• ").append(section.getName()).append("\n");
            }
        }
        
        AlertUtils.showInfo("Dettagli Menu", "Dettagli di: " + selected.getName(), content.toString());
    }
    
    public void refreshData() {
        menuListView.getItems().clear();
        menuListView.getItems().addAll(controller.getAllMenus());
    }
}