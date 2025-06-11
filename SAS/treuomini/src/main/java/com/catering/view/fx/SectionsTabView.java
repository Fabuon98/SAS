// SectionsTabView.java - Gestione della tab Sezioni
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Section;
import com.catering.model.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SectionsTabView {
    private ChefController controller;
    private ListView<Section> sectionListView;
    
    public SectionsTabView(ChefController controller) {
        this.controller = controller;
        this.sectionListView = new ListView<>();
    }
    
    public Tab createTab() {
        Tab tab = new Tab("Sezioni");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }
    
    private VBox createContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Sezioni Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        sectionListView.setPrefHeight(300);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addSectionBtn = new Button("Aggiungi Sezione");
        Button editSectionBtn = new Button("Modifica Sezione");
        Button deleteSectionBtn = new Button("Elimina Sezione");
        Button viewSectionRecipesBtn = new Button("Visualizza Ricette");
        
        buttons.getChildren().addAll(addSectionBtn, editSectionBtn, deleteSectionBtn, viewSectionRecipesBtn);
        
        addSectionBtn.setOnAction(e -> showAddSectionDialog());
        editSectionBtn.setOnAction(e -> showEditSectionDialog());
        deleteSectionBtn.setOnAction(e -> deleteSelectedSection());
        viewSectionRecipesBtn.setOnAction(e -> showSectionRecipes());
        
        content.getChildren().addAll(title, sectionListView, buttons);
        return content;
    }
    
    private void showAddSectionDialog() {
        SectionDialog dialog = new SectionDialog(controller, null);
        dialog.showAndWait().ifPresent(section -> {
            refreshData();
            AlertUtils.showSuccess("Sezione creata con successo!");
        });
    }
    
    private void showEditSectionDialog() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione da modificare.");
            return;
        }
        
        SectionDialog dialog = new SectionDialog(controller, selected);
        dialog.showAndWait().ifPresent(section -> {
            refreshData();
            AlertUtils.showSuccess("Sezione modificata con successo!");
        });
    }
    
    private void deleteSelectedSection() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione da eliminare.");
            return;
        }
        
        if (AlertUtils.confirmDelete("sezione", selected.getName())) {
            controller.deleteSection(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Sezione eliminata con successo!");
        }
    }
    
    private void showSectionRecipes() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione per visualizzare le sue ricette.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        if (selected.getRecipes().isEmpty()) {
            content.append("Nessuna ricetta collegata a questa sezione.");
        } else {
            content.append("Ricette collegate:\n\n");
            for (Recipe recipe : selected.getRecipes()) {
                content.append("• ").append(recipe.getName()).append("\n");
                if (recipe.getDifficulty() != null) {
                    content.append("  Difficoltà: ").append(recipe.getDifficulty()).append("\n");
                }
                if (recipe.getPreparationTime() > 0) {
                    content.append("  Tempo preparazione: ").append(recipe.getPreparationTime()).append(" min\n");
                }
                content.append("\n");
            }
        }
        
        AlertUtils.showInfo("Ricette della Sezione", "Ricette della sezione: " + selected.getName(), content.toString());
    }
    
    public void refreshData() {
        sectionListView.getItems().clear();
        sectionListView.getItems().addAll(controller.getAllSections());
    }
}