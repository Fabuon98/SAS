// IngredientsDialog.java - Dialog per gestire ingredienti
package com.catering.view.fx;

import com.catering.model.Recipe;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class IngredientsDialog extends Dialog<Void> {
    private Recipe recipe;
    private ListView<String> ingredientsList;
    private TextField newIngredientField;
    
    public IngredientsDialog(Recipe recipe) {
        this.recipe = recipe;
        setupDialog();
    }
    
    private void setupDialog() {
        setTitle("Gestione Ingredienti");
        setHeaderText("Ingredienti per: " + recipe.getName());
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Lista ingredienti
        ingredientsList = new ListView<>();
        ingredientsList.getItems().addAll(recipe.getIngredients());
        ingredientsList.setPrefHeight(200);
        
        // Campo per nuovo ingrediente
        newIngredientField = new TextField();
        newIngredientField.setPromptText("Nuovo ingrediente...");
        
        // Bottoni
        Button addIngredientBtn = new Button("Aggiungi");
        Button removeIngredientBtn = new Button("Rimuovi Selezionato");
        
        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(newIngredientField, addIngredientBtn, removeIngredientBtn);
        
        // Eventi bottoni
        addIngredientBtn.setOnAction(e -> addIngredient());
        removeIngredientBtn.setOnAction(e -> removeSelectedIngredient());
        
        // Permetti di aggiungere ingredienti premendo Enter
        newIngredientField.setOnAction(e -> addIngredient());
        
        content.getChildren().addAll(
            new Label("Lista Ingredienti:"),
            ingredientsList,
            inputBox
        );
        
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }
    
    private void addIngredient() {
        String newIngredient = newIngredientField.getText().trim();
        if (!newIngredient.isEmpty()) {
            // Controlla se l'ingrediente esiste già
            if (recipe.getIngredients().contains(newIngredient)) {
                AlertUtils.showWarning("Questo ingrediente è già presente nella lista.");
                return;
            }
            
            // Aggiungi l'ingrediente
            recipe.addIngredient(newIngredient);
            ingredientsList.getItems().add(newIngredient);
            newIngredientField.clear();
        } else {
            AlertUtils.showWarning("Inserisci il nome dell'ingrediente.");
        }
    }
    
    private void removeSelectedIngredient() {
        String selectedIngredient = ingredientsList.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            if (AlertUtils.confirm("Conferma Rimozione", 
                    "Rimuovere ingrediente?", 
                    "Sei sicuro di voler rimuovere \"" + selectedIngredient + "\"?")) {
                
                recipe.getIngredients().remove(selectedIngredient);
                ingredientsList.getItems().remove(selectedIngredient);
            }
        } else {
            AlertUtils.showWarning("Seleziona un ingrediente da rimuovere.");
        }
    }
}