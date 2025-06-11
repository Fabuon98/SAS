// RecipeDialog.java - Dialog per gestire ricette
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Recipe;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class RecipeDialog extends Dialog<Recipe> {
    private ChefController controller;
    private Recipe recipe; // null per nuova ricetta, oggetto esistente per modifica
    
    private TextField nameField;
    private TextArea descriptionArea;
    private TextField prepTimeField;
    private TextField cookTimeField;
    private TextField servingsField;
    private ComboBox<String> difficultyCombo;
    
    public RecipeDialog(ChefController controller, Recipe recipe) {
        this.controller = controller;
        this.recipe = recipe;
        setupDialog();
    }
    
    private void setupDialog() {
        boolean isEdit = (recipe != null);
        
        setTitle(isEdit ? "Modifica Ricetta" : "Aggiungi Nuova Ricetta");
        setHeaderText(isEdit ? "Modifica i dettagli della ricetta" : "Inserisci i dettagli della nuova ricetta");
        
        // Crea i campi del form
        createFormFields(isEdit);
        
        // Crea il layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        grid.add(new Label("Nome Ricetta:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Tempo prep. (min):"), 0, 2);
        grid.add(prepTimeField, 1, 2);
        grid.add(new Label("Tempo cottura (min):"), 0, 3);
        grid.add(cookTimeField, 1, 3);
        grid.add(new Label("Porzioni:"), 0, 4);
        grid.add(servingsField, 1, 4);
        grid.add(new Label("Difficoltà:"), 0, 5);
        grid.add(difficultyCombo, 1, 5);
        
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Imposta il converter per gestire il risultato
        setResultConverter(this::processResult);
    }
    
    private void createFormFields(boolean isEdit) {
        nameField = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(2);
        prepTimeField = new TextField();
        cookTimeField = new TextField();
        servingsField = new TextField();
        
        difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("Facile", "Medio", "Difficile");
        difficultyCombo.setValue("Facile");
        
        if (isEdit) {
            // Popola i campi con i dati esistenti
            nameField.setText(recipe.getName());
            descriptionArea.setText(recipe.getDescription());
            prepTimeField.setText(String.valueOf(recipe.getPreparationTime()));
            cookTimeField.setText(String.valueOf(recipe.getCookingTime()));
            servingsField.setText(String.valueOf(recipe.getServings()));
            difficultyCombo.setValue(recipe.getDifficulty() != null ? recipe.getDifficulty() : "Facile");
        }
    }
    
    private Recipe processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                // Validazione nome obbligatorio
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome della ricetta non può essere vuoto.");
                    return null;
                }
                
                // Parsing dei campi numerici (con valori di default)
                int prepTime = 0;
                int cookTime = 0;
                int servings = 0;
                
                if (!prepTimeField.getText().trim().isEmpty()) {
                    prepTime = Integer.parseInt(prepTimeField.getText().trim());
                    if (prepTime < 0) {
                        AlertUtils.showError("Il tempo di preparazione non può essere negativo.");
                        return null;
                    }
                }
                
                if (!cookTimeField.getText().trim().isEmpty()) {
                    cookTime = Integer.parseInt(cookTimeField.getText().trim());
                    if (cookTime < 0) {
                        AlertUtils.showError("Il tempo di cottura non può essere negativo.");
                        return null;
                    }
                }
                
                if (!servingsField.getText().trim().isEmpty()) {
                    servings = Integer.parseInt(servingsField.getText().trim());
                    if (servings < 0) {
                        AlertUtils.showError("Il numero di porzioni non può essere negativo.");
                        return null;
                    }
                }
                
                String difficulty = difficultyCombo.getValue();
                
                // Crea o aggiorna la ricetta
                if (recipe == null) {
                    // Nuova ricetta
                    Recipe newRecipe = controller.createRecipe(name, description);
                    newRecipe.setPreparationTime(prepTime);
                    newRecipe.setCookingTime(cookTime);
                    newRecipe.setServings(servings);
                    newRecipe.setDifficulty(difficulty);
                    return newRecipe;
                } else {
                    // Modifica ricetta esistente
                    recipe.setName(name);
                    recipe.setDescription(description);
                    recipe.setPreparationTime(prepTime);
                    recipe.setCookingTime(cookTime);
                    recipe.setServings(servings);
                    recipe.setDifficulty(difficulty);
                    controller.updateRecipe(recipe);
                    return recipe;
                }
                
            } catch (NumberFormatException e) {
                AlertUtils.showError("I campi numerici devono contenere numeri validi.");
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        return null;
    }
}