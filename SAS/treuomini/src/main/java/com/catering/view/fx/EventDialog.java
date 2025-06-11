// EventDialog.java - Dialog per gestire eventi
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Event;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.util.Optional;

public class EventDialog extends Dialog<Event> {
    private ChefController controller;
    private Event event; // null per nuovo evento, oggetto esistente per modifica
    
    private TextField nameField;
    private DatePicker datePicker;
    private TextField locationField;
    private TextField guestsField;
    
    public EventDialog(ChefController controller, Event event) {
        this.controller = controller;
        this.event = event;
        setupDialog();
    }
    
    private void setupDialog() {
        boolean isEdit = (event != null);
        
        setTitle(isEdit ? "Modifica Evento" : "Aggiungi Nuovo Evento");
        setHeaderText(isEdit ? "Modifica i dettagli dell'evento" : "Inserisci i dettagli del nuovo evento");
        
        // Crea i campi del form
        createFormFields(isEdit);
        
        // Crea il layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        grid.add(new Label("Nome Evento:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Località:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Numero Ospiti:"), 0, 3);
        grid.add(guestsField, 1, 3);
        
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Imposta il converter per gestire il risultato
        setResultConverter(this::processResult);
    }
    
    private void createFormFields(boolean isEdit) {
        nameField = new TextField();
        datePicker = new DatePicker();
        locationField = new TextField();
        guestsField = new TextField();
        
        if (isEdit) {
            // Popola i campi con i dati esistenti
            nameField.setText(event.getName());
            datePicker.setValue(event.getDate());
            locationField.setText(event.getLocation());
            guestsField.setText(String.valueOf(event.getNumberOfGuests()));
        } else {
            // Valori di default per nuovo evento
            datePicker.setValue(LocalDate.now());
        }
    }
    
    private Event processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                String name = nameField.getText().trim();
                LocalDate date = datePicker.getValue();
                String location = locationField.getText().trim();
                int guests = Integer.parseInt(guestsField.getText().trim());
                
                // Validazione
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome dell'evento non può essere vuoto.");
                    return null;
                }
                
                if (location.isEmpty()) {
                    AlertUtils.showError("La località non può essere vuota.");
                    return null;
                }
                
                if (guests <= 0) {
                    AlertUtils.showError("Il numero di ospiti deve essere maggiore di zero.");
                    return null;
                }
                
                if (date == null) {
                    AlertUtils.showError("Seleziona una data valida.");
                    return null;
                }
                
                // Crea o aggiorna l'evento
                if (event == null) {
                    // Nuovo evento
                    return controller.createEvent(name, date, location, guests);
                } else {
                    // Modifica evento esistente
                    event.setName(name);
                    event.setDate(date);
                    event.setLocation(location);
                    event.setNumberOfGuests(guests);
                    controller.updateEvent(event);
                    return event;
                }
                
            } catch (NumberFormatException e) {
                AlertUtils.showError("Il numero di ospiti deve essere un numero valido.");
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        return null;
    }
}