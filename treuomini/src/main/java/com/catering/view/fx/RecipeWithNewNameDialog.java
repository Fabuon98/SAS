// RecipeWithNewNameDialog.java - Dialog per collegare ricetta con nuovo nome
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Recipe;
import com.catering.model.Section;
import com.catering.model.Preparation;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RecipeWithNewNameDialog extends Dialog<Boolean> {
    private ChefController controller;
    private Section section;
    private Recipe originalRecipe;
    
    private TextField newNameField;
    
    public RecipeWithNewNameDialog(ChefController controller, Section section, Recipe originalRecipe) {
        this.controller = controller;
        this.section = section;
        this.originalRecipe = originalRecipe;
        setupDialog();
    }
    
    private void setupDialog() {
        setTitle("Collega Ricetta con Nuovo Nome");
        setHeaderText("Collega \"" + originalRecipe.getName() + "\" alla sezione \"" + section.getName() + "\" con un nuovo nome");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Informazioni ricetta originale
        Label originalInfo = new Label("Ricetta originale: " + originalRecipe.getName());
        originalInfo.setStyle("-fx-font-weight: bold;");
        
        // Campo per il nuovo nome
        newNameField = new TextField(originalRecipe.getName());
        newNameField.setPromptText("Inserisci il nuovo nome per questa ricetta...");
        
        // Area di riepilogo dei dettagli che verranno mantenuti
        Label detailsLabel = new Label("Dettagli che verranno mantenuti:");
        detailsLabel.setStyle("-fx-font-weight: bold;");
        
        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(6);
        
        StringBuilder details = new StringBuilder();
        details.append("Descrizione: ").append(originalRecipe.getDescription()).append("\n");
        details.append("Tempo preparazione: ").append(originalRecipe.getPreparationTime()).append(" min\n");
        details.append("Tempo cottura: ").append(originalRecipe.getCookingTime()).append(" min\n");
        details.append("Porzioni: ").append(originalRecipe.getServings()).append("\n");
        details.append("Difficoltà: ").append(originalRecipe.getDifficulty()).append("\n");
        details.append("Ingredienti: ").append(originalRecipe.getIngredients().size()).append(" elementi\n");
        details.append("Preparazioni: ").append(originalRecipe.getPreparations().size()).append(" passaggi");
        
        detailsArea.setText(details.toString());
        
        content.getChildren().addAll(
            originalInfo,
            new Separator(),
            new Label("Nuovo nome:"),
            newNameField,
            new Separator(),
            detailsLabel,
            detailsArea
        );
        
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        setResultConverter(this::processResult);
    }
    
    private Boolean processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            String newName = newNameField.getText().trim();
            
            if (newName.isEmpty()) {
                AlertUtils.showError("Il nome non può essere vuoto.");
                return false;
            }
            
            try {
                if (!newName.equals(originalRecipe.getName())) {
                    // Crea una copia della ricetta con il nuovo nome
                    Recipe recipeWithNewName = createRecipeCopy(originalRecipe, newName);
                    
                    // Collega la nuova ricetta alla sezione
                    controller.addRecipeToSection(section.getId(), recipeWithNewName.getId());
                    
                    AlertUtils.showSuccess(
                        "Ricetta \"" + newName + "\" creata e collegata alla sezione \"" + section.getName() + "\"!\n" +
                        "La ricetta originale \"" + originalRecipe.getName() + "\" rimane invariata.");
                } else {
                    // Se il nome è uguale, fai il collegamento normale
                    controller.addRecipeToSection(section.getId(), originalRecipe.getId());
                    AlertUtils.showSuccess("Ricetta collegata alla sezione con successo!");
                }
                
                return true;
                
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il collegamento: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    // Metodo helper per creare una copia di una ricetta con un nuovo nome
    private Recipe createRecipeCopy(Recipe original, String newName) {
        // Crea una nuova ricetta con il nuovo nome
        Recipe copy = controller.createRecipe(newName, original.getDescription());
        
        // Copia tutti i dettagli
        copy.setPreparationTime(original.getPreparationTime());
        copy.setCookingTime(original.getCookingTime());
        copy.setServings(original.getServings());
        copy.setDifficulty(original.getDifficulty());
        
        // Copia gli ingredienti
        for (String ingredient : original.getIngredients()) {
            copy.addIngredient(ingredient);
        }
        
        // Copia le preparazioni
        for (Preparation prep : original.getPreparations()) {
            Preparation newPrep = controller.createPreparation(
                prep.getStepNumber(), 
                prep.getDescription(), 
                prep.getTimeInMinutes()
            );
            copy.addPreparation(newPrep);
        }
        
        return copy;
    }
}