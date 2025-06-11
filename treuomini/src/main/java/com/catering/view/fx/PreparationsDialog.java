// PreparationsDialog.java - Dialog per gestire preparazioni
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Recipe;
import com.catering.model.Preparation;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PreparationsDialog extends Dialog<Void> {
    private ChefController controller;
    private Recipe recipe;
    private ListView<Preparation> preparationsList;
    
    // Campi del form
    private TextField stepField;
    private TextArea descriptionArea;
    private TextField timeField;
    
    public PreparationsDialog(ChefController controller, Recipe recipe) {
        this.controller = controller;
        this.recipe = recipe;
        setupDialog();
    }
    
    private void setupDialog() {
        setTitle("Gestione Preparazioni");
        setHeaderText("Preparazioni per: " + recipe.getName());
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Lista preparazioni
        preparationsList = new ListView<>();
        preparationsList.getItems().addAll(recipe.getPreparations());
        preparationsList.setPrefHeight(200);
        
        // Form per nuova preparazione
        GridPane prepForm = createPreparationForm();
        
        // Bottoni
        HBox buttonBox = new HBox(10);
        
        Button addPrepBtn = new Button("Aggiungi Preparazione");
        Button removePrepBtn = new Button("Rimuovi Selezionata");
        Button editPrepBtn = new Button("Modifica Selezionata");
        Button clearFormBtn = new Button("Pulisci Form");
        
        buttonBox.getChildren().addAll(addPrepBtn, removePrepBtn, editPrepBtn, clearFormBtn);
        
        // Eventi bottoni
        addPrepBtn.setOnAction(e -> addPreparation());
        removePrepBtn.setOnAction(e -> removeSelectedPreparation());
        editPrepBtn.setOnAction(e -> editSelectedPreparation());
        clearFormBtn.setOnAction(e -> clearForm());
        
        content.getChildren().addAll(
            new Label("Lista Preparazioni:"),
            preparationsList,
            new Separator(),
            new Label("Aggiungi/Modifica Preparazione:"),
            prepForm,
            buttonBox
        );
        
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }
    
    private GridPane createPreparationForm() {
        GridPane prepForm = new GridPane();
        prepForm.setHgap(10);
        prepForm.setVgap(10);
        
        stepField = new TextField();
        stepField.setPromptText("Numero step");
        
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descrizione del passaggio...");
        descriptionArea.setPrefRowCount(2);
        
        timeField = new TextField();
        timeField.setPromptText("Tempo in minuti");
        
        prepForm.add(new Label("Step:"), 0, 0);
        prepForm.add(stepField, 1, 0);
        prepForm.add(new Label("Descrizione:"), 0, 1);
        prepForm.add(descriptionArea, 1, 1);
        prepForm.add(new Label("Tempo (min):"), 0, 2);
        prepForm.add(timeField, 1, 2);
        
        return prepForm;
    }
    
    private void addPreparation() {
        try {
            String stepText = stepField.getText().trim();
            String description = descriptionArea.getText().trim();
            String timeText = timeField.getText().trim();
            
            // Validazioni
            if (stepText.isEmpty()) {
                AlertUtils.showError("Inserisci il numero dello step.");
                return;
            }
            
            if (description.isEmpty()) {
                AlertUtils.showError("Inserisci la descrizione della preparazione.");
                return;
            }
            
            if (timeText.isEmpty()) {
                AlertUtils.showError("Inserisci il tempo di preparazione.");
                return;
            }
            
            int step = Integer.parseInt(stepText);
            int time = Integer.parseInt(timeText);
            
            if (step <= 0) {
                AlertUtils.showError("Il numero dello step deve essere maggiore di zero.");
                return;
            }
            
            if (time < 0) {
                AlertUtils.showError("Il tempo non può essere negativo.");
                return;
            }
            
            // Controlla se esiste già uno step con questo numero
            boolean stepExists = recipe.getPreparations().stream()
                .anyMatch(prep -> prep.getStepNumber() == step);
            
            if (stepExists) {
                AlertUtils.showError("Esiste già una preparazione con step numero " + step + ".");
                return;
            }
            
            // Crea e aggiungi la preparazione
            Preparation prep = controller.createPreparation(step, description, time);
            recipe.addPreparation(prep);
            preparationsList.getItems().add(prep);
            
            // Pulisci i campi
            clearForm();
            
            // Ordina la lista per numero step
            sortPreparationsList();
            
            AlertUtils.showSuccess("Preparazione aggiunta con successo!");
            
        } catch (NumberFormatException e) {
            AlertUtils.showError("Step e tempo devono essere numeri validi.");
        } catch (Exception e) {
            AlertUtils.showError("Errore durante l'aggiunta: " + e.getMessage());
        }
    }
    
    private void removeSelectedPreparation() {
        Preparation selectedPrep = preparationsList.getSelectionModel().getSelectedItem();
        if (selectedPrep != null) {
            if (AlertUtils.confirm("Conferma Rimozione", 
                    "Rimuovere preparazione?", 
                    "Sei sicuro di voler rimuovere lo step " + selectedPrep.getStepNumber() + ":\n\"" + 
                    selectedPrep.getDescription() + "\"?")) {
                
                recipe.getPreparations().remove(selectedPrep);
                preparationsList.getItems().remove(selectedPrep);
                AlertUtils.showSuccess("Preparazione rimossa con successo!");
            }
        } else {
            AlertUtils.showWarning("Seleziona una preparazione da rimuovere.");
        }
    }
    
    private void editSelectedPreparation() {
        Preparation selectedPrep = preparationsList.getSelectionModel().getSelectedItem();
        if (selectedPrep != null) {
            // Carica i dati nei campi del form
            stepField.setText(String.valueOf(selectedPrep.getStepNumber()));
            descriptionArea.setText(selectedPrep.getDescription());
            timeField.setText(String.valueOf(selectedPrep.getTimeInMinutes()));
            
            // Rimuovi la preparazione esistente così può essere ri-aggiunta modificata
            recipe.getPreparations().remove(selectedPrep);
            preparationsList.getItems().remove(selectedPrep);
            
            AlertUtils.showInfo("Modifica Preparazione", 
                "Preparazione caricata", 
                "I dati della preparazione sono stati caricati nel form.\n" +
                "Modifica i campi necessari e clicca 'Aggiungi Preparazione' per salvare le modifiche.");
        } else {
            AlertUtils.showWarning("Seleziona una preparazione da modificare.");
        }
    }
    
    private void clearForm() {
        stepField.clear();
        descriptionArea.clear();
        timeField.clear();
    }
    
    private void sortPreparationsList() {
        // Ordina la lista delle preparazioni per numero step
        preparationsList.getItems().sort((p1, p2) -> 
            Integer.compare(p1.getStepNumber(), p2.getStepNumber()));
    }
    
    private int getNextAvailableStep() {
        // Trova il prossimo numero step disponibile
        int maxStep = recipe.getPreparations().stream()
            .mapToInt(Preparation::getStepNumber)
            .max()
            .orElse(0);
        return maxStep + 1;
    }
    
    private void suggestNextStep() {
        // Suggerisce automaticamente il prossimo step number
        if (stepField.getText().trim().isEmpty()) {
            stepField.setText(String.valueOf(getNextAvailableStep()));
        }
    }
}