// SectionDialog.java - Dialog per gestire sezioni
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Section;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class SectionDialog extends Dialog<Section> {
    private ChefController controller;
    private Section section; // null per nuova sezione, oggetto esistente per modifica
    
    private TextField nameField;
    private TextArea descriptionArea;
    
    public SectionDialog(ChefController controller, Section section) {
        this.controller = controller;
        this.section = section;
        setupDialog();
    }
    
    private void setupDialog() {
        boolean isEdit = (section != null);
        
        setTitle(isEdit ? "Modifica Sezione" : "Aggiungi Nuova Sezione");
        setHeaderText(isEdit ? "Modifica i dettagli della sezione" : "Inserisci i dettagli della nuova sezione");
        
        // Crea i campi del form
        createFormFields(isEdit);
        
        // Crea il layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        grid.add(new Label("Nome Sezione:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Imposta il converter per gestire il risultato
        setResultConverter(this::processResult);
    }
    
    private void createFormFields(boolean isEdit) {
        nameField = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        
        if (isEdit) {
            // Popola i campi con i dati esistenti
            nameField.setText(section.getName());
            descriptionArea.setText(section.getDescription());
        }
    }
    
    private Section processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                // Validazione
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome della sezione non può essere vuoto.");
                    return null;
                }
                
                // Crea o aggiorna la sezione
                if (section == null) {
                    // Nuova sezione
                    return controller.createSection(name, description);
                } else {
                    // Modifica sezione esistente
                    section.setName(name);
                    section.setDescription(description);
                    controller.updateSection(section);
                    return section;
                }
                
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        return null;
    }
}