// RecipesTabView.java - Gestione della tab Ricette
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RecipesTabView {
    private ChefController controller;
    private ListView<Recipe> recipeListView;
    
    public RecipesTabView(ChefController controller) {
        this.controller = controller;
        this.recipeListView = new ListView<>();
    }
    
    public Tab createTab() {
        Tab tab = new Tab("Ricette");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }
    
    private VBox createContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Ricette");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        recipeListView.setPrefHeight(300);
        
        // Prima riga di bottoni
        HBox firstRowButtons = new HBox(10);
        firstRowButtons.setAlignment(Pos.CENTER);
        
        Button addRecipeBtn = new Button("Aggiungi Ricetta");
        Button editRecipeBtn = new Button("Modifica Ricetta");
        Button deleteRecipeBtn = new Button("Elimina Ricetta");
        
        firstRowButtons.getChildren().addAll(addRecipeBtn, editRecipeBtn, deleteRecipeBtn);
        
        // Seconda riga di bottoni
        HBox secondRowButtons = new HBox(10);
        secondRowButtons.setAlignment(Pos.CENTER);
        
        Button viewRecipeBtn = new Button("Visualizza Ricetta");
        Button editIngredientsBtn = new Button("Gestisci Ingredienti");
        Button editPreparationsBtn = new Button("Gestisci Preparazioni");
        
        secondRowButtons.getChildren().addAll(viewRecipeBtn, editIngredientsBtn, editPreparationsBtn);
        
        // Eventi bottoni
        addRecipeBtn.setOnAction(e -> showAddRecipeDialog());
        editRecipeBtn.setOnAction(e -> showEditRecipeDialog());
        deleteRecipeBtn.setOnAction(e -> deleteSelectedRecipe());
        viewRecipeBtn.setOnAction(e -> showRecipeDetails());
        editIngredientsBtn.setOnAction(e -> showIngredientsDialog());
        editPreparationsBtn.setOnAction(e -> showPreparationsDialog());
        
        content.getChildren().addAll(title, recipeListView, firstRowButtons, secondRowButtons);
        return content;
    }
    
    private void showAddRecipeDialog() {
        RecipeDialog dialog = new RecipeDialog(controller, null);
        dialog.showAndWait().ifPresent(recipe -> {
            refreshData();
            AlertUtils.showSuccess("Ricetta creata con successo!");
        });
    }
    
    private void showEditRecipeDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da modificare.");
            return;
        }
        
        RecipeDialog dialog = new RecipeDialog(controller, selected);
        dialog.showAndWait().ifPresent(recipe -> {
            refreshData();
            AlertUtils.showSuccess("Ricetta modificata con successo!");
        });
    }
    
    private void deleteSelectedRecipe() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da eliminare.");
            return;
        }
        
        if (AlertUtils.confirmDelete("ricetta", selected.getName())) {
            controller.deleteRecipe(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Ricetta eliminata con successo!");
        }
    }
    
    private void showRecipeDetails() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da visualizzare.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        content.append("Descrizione: ").append(selected.getDescription()).append("\n");
        content.append("Tempo preparazione: ").append(selected.getPreparationTime()).append(" min\n");
        content.append("Tempo cottura: ").append(selected.getCookingTime()).append(" min\n");
        content.append("Porzioni: ").append(selected.getServings()).append("\n");
        content.append("Difficoltà: ").append(selected.getDifficulty()).append("\n");
        content.append("Ingredienti: ").append(selected.getIngredients().size()).append("\n");
        content.append("Preparazioni: ").append(selected.getPreparations().size());
        
        AlertUtils.showInfo("Dettagli Ricetta", "Ricetta: " + selected.getName(), content.toString());
    }
    
    private void showIngredientsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta per gestire gli ingredienti.");
            return;
        }
        
        IngredientsDialog dialog = new IngredientsDialog(selected);
        dialog.showAndWait();
    }
    
    private void showPreparationsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta per gestire le preparazioni.");
            return;
        }
        
        PreparationsDialog dialog = new PreparationsDialog(controller, selected);
        dialog.showAndWait();
    }
    
    public void refreshData() {
        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(controller.getAllRecipes());
    }
}