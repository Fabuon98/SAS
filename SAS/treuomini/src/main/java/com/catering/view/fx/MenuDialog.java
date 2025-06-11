// MenuDialog.java - Dialog per gestire menu
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Menu;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class MenuDialog extends Dialog<Menu> {
    private ChefController controller;
    private Menu menu; // null per nuovo menu, oggetto esistente per modifica
    
    private TextField nameField;
    private TextArea descriptionArea;
    private TextField priceField;
    
    public MenuDialog(ChefController controller, Menu menu) {
        this.controller = controller;
        this.menu = menu;
        setupDialog();
    }
    
    private void setupDialog() {
        boolean isEdit = (menu != null);
        
        setTitle(isEdit ? "Modifica Menu" : "Aggiungi Nuovo Menu");
        setHeaderText(isEdit ? "Modifica i dettagli del menu" : "Inserisci i dettagli del nuovo menu");
        
        // Crea i campi del form
        createFormFields(isEdit);
        
        // Crea il layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        grid.add(new Label("Nome Menu:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Prezzo (€):"), 0, 2);
        grid.add(priceField, 1, 2);
        
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Imposta il converter per gestire il risultato
        setResultConverter(this::processResult);
    }
    
    private void createFormFields(boolean isEdit) {
        nameField = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        priceField = new TextField();
        
        if (isEdit) {
            // Popola i campi con i dati esistenti
            nameField.setText(menu.getName());
            descriptionArea.setText(menu.getDescription());
            priceField.setText(String.valueOf(menu.getPrice()));
        }
    }
    
    private Menu processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                
                // Validazione
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome del menu non può essere vuoto.");
                    return null;
                }
                
                if (price < 0) {
                    AlertUtils.showError("Il prezzo non può essere negativo.");
                    return null;
                }
                
                // Crea o aggiorna il menu
                if (menu == null) {
                    // Nuovo menu
                    return controller.createMenu(name, description, price);
                } else {
                    // Modifica menu esistente
                    menu.setName(name);
                    menu.setDescription(description);
                    menu.setPrice(price);
                    controller.updateMenu(menu);
                    return menu;
                }
                
            } catch (NumberFormatException e) {
                AlertUtils.showError("Il prezzo deve essere un numero valido.");
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        return null;
    }
}