// EventDialog.java - Dialog JavaFX per creare o modificare un oggetto Event
package com.catering.view.fx;

import java.time.LocalDate;    // Controller per operazioni CRUD sugli Event

import com.catering.controller.ChefController;                  // Model dell’evento
import com.catering.model.Event;                    // Per padding nel GridPane

import javafx.geometry.Insets;                    // Dialog, Label, TextField, DatePicker, ButtonType
import javafx.scene.control.ButtonType;              // Layout a griglia
import javafx.scene.control.DatePicker;                       // Data senza orario
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Dialog personalizzato per la gestione di un Event.
 * Può operare in due modalità:
 *  - aggiunta di un nuovo evento (event == null)
 *  - modifica di un evento esistente (event != null)
 *
 * Raccoglie nome, data, località e numero di ospiti,
 * valida i valori inseriti e invoca il controller per persistere.
 */
public class EventDialog extends Dialog<Event> {
    private final ChefController controller;   // Riferimento al controller
    private final Event event;                 // Se null: nuovo, altrimenti modifica
    
    // Campi del form
    private TextField nameField;               // Per il nome dell'evento
    private DatePicker datePicker;             // Per la data
    private TextField locationField;           // Per la località
    private TextField guestsField;             // Per il numero di ospiti
    
    /**
     * Costruttore.
     * @param controller  controller per creare o aggiornare l'evento
     * @param event       evento esistente da modificare, o null per creazione
     */
    public EventDialog(ChefController controller, Event event) {
        this.controller = controller;
        this.event = event;
        setupDialog();                          // Configura titolo, form e pulsanti
    }
    
    /**
     * Configura titolo, header, campi e tasti di conferma/annulla,
     * e definisce come trasformare i valori inseriti in un Event.
     */
    private void setupDialog() {
        boolean isEdit = (event != null);
        
        // Titolo e testo di intestazione in base a modalità (edit vs new)
        setTitle(isEdit ? "Modifica Evento" : "Aggiungi Nuovo Evento");
        setHeaderText(isEdit
            ? "Modifica i dettagli dell'evento"
            : "Inserisci i dettagli del nuovo evento");
        
        // Crea i controlli del form, popolandoli se in modifica
        createFormFields(isEdit);
        
        // Costruisce il layout a griglia per disporre label + campi
        GridPane grid = new GridPane();
        grid.setHgap(10);                      // Spazio orizzontale tra colonne
        grid.setVgap(10);                      // Spazio verticale tra righe
        grid.setPadding(new Insets(20));       // Margine interno
        
        // Aggiunge label e campi nel GridPane (colonna, riga)
        grid.add(new Label("Nome Evento:"),     0, 0);
        grid.add(nameField,                     1, 0);
        grid.add(new Label("Data:"),            0, 1);
        grid.add(datePicker,                    1, 1);
        grid.add(new Label("Località:"),        0, 2);
        grid.add(locationField,                 1, 2);
        grid.add(new Label("Numero Ospiti:"),   0, 3);
        grid.add(guestsField,                   1, 3);
        
        // Imposta il contenuto del dialog e i pulsanti OK/Cancel
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Converte il risultato del dialog in un oggetto Event
        setResultConverter(this::processResult);
    }
    
    /**
     * Crea e inizializza i campi form.
     * Se isEdit==true, carica i valori dell'evento esistente.
     * Altrimenti imposta default (data corrente).
     */
    private void createFormFields(boolean isEdit) {
        nameField     = new TextField();
        datePicker    = new DatePicker();
        locationField = new TextField();
        guestsField   = new TextField();
        
        if (isEdit) {
            // Popola con i dati esistenti dell'evento
            nameField.setText(event.getName());
            datePicker.setValue(event.getDate());
            locationField.setText(event.getLocation());
            guestsField.setText(String.valueOf(event.getNumberOfGuests()));
        } else {
            // Imposta la data di default a oggi per nuovi eventi
            datePicker.setValue(LocalDate.now());
        }
    }
    
    /**
     * Callback invocato quando l'utente preme OK o CANCEL.
     * Se OK, valida i campi, crea o aggiorna l'evento tramite controller.
     * @param buttonType  tipo di pulsante premuto
     * @return l'oggetto Event creato/modificato, o null per annulla/errore
     */
    private Event processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                // Legge e pulisce i valori dai campi
                String name     = nameField.getText().trim();
                LocalDate date  = datePicker.getValue();
                String location = locationField.getText().trim();
                int guests      = Integer.parseInt(guestsField.getText().trim());
                
                // Validazioni di base sui campi obbligatori
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
                
                // Se event==null, creazione: altrimenti aggiornamento
                if (event == null) {
                    return controller.createEvent(name, date, location, guests);
                } else {
                    // Modifica in-place dell'oggetto esistente
                    event.setName(name);
                    event.setDate(date);
                    event.setLocation(location);
                    event.setNumberOfGuests(guests);
                    controller.updateEvent(event);
                    return event;
                }
                
            } catch (NumberFormatException e) {
                // Gestisce conversione non valida di numero ospiti
                AlertUtils.showError("Il numero di ospiti deve essere un numero valido.");
            } catch (Exception e) {
                // Qualsiasi altra eccezione in fase di salvataggio
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        // Ritorna null se Cancel o validazione fallita
        return null;
    }
}
