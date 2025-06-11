// MenuDialog.java - Dialog JavaFX per creare o modificare un oggetto Menu
package com.catering.view.fx;

import com.catering.controller.ChefController;    // Controller per operazioni CRUD sui Menu
import com.catering.model.Menu;                  // Model del menu

import javafx.geometry.Insets;                   // Per padding nei layout
import javafx.scene.control.ButtonType;                   // Dialog, Label, TextField, TextArea, ButtonType
import javafx.scene.control.Dialog;             // Layout a griglia
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Dialog personalizzato per la gestione di un Menu.
 * Può operare in due modalità:
 *  - creazione di un nuovo menu (menu == null)
 *  - modifica di un menu esistente (menu != null)
 *
 * Raccoglie nome, descrizione e prezzo, valida i dati inseriti
 * e invoca il controller per salvare o aggiornare il model Menu.
 */
public class MenuDialog extends Dialog<Menu> {
    private final ChefController controller;       // Riferimento al controller condiviso
    private final Menu menu;                       // Se null: creazione; altrimenti modifica

    // Campi del form
    private TextField nameField;                   // Per il nome del menu
    private TextArea descriptionArea;              // Per la descrizione del menu
    private TextField priceField;                  // Per il prezzo del menu

    /**
     * Costruttore.
     * @param controller istanza di ChefController per le operazioni CRUD
     * @param menu       oggetto Menu esistente da modificare, o null per creazione
     */
    public MenuDialog(ChefController controller, Menu menu) {
        this.controller = controller;
        this.menu = menu;
        setupDialog();                              // Configura titolo, contenuto e pulsanti
    }

    /**
     * Configura il dialog:
     *  - assegna titolo e header in base a creazione o modifica
     *  - genera i campi del form e li popola se in modalità edit
     *  - costruisce il layout a griglia
     *  - aggiunge i pulsanti OK/Cancel
     *  - definisce il converter per produrre l'oggetto Menu al submit
     */
    private void setupDialog() {
        boolean isEdit = (menu != null);

        // Titolo e header dinamici
        setTitle(isEdit ? "Modifica Menu" : "Aggiungi Nuovo Menu");
        setHeaderText(isEdit
            ? "Modifica i dettagli del menu"
            : "Inserisci i dettagli del nuovo menu");

        // Crea e inizializza i campi del form
        createFormFields(isEdit);

        // Layout a griglia per disporre etichette e campi
        GridPane grid = new GridPane();
        grid.setHgap(10);                          // Spazio orizzontale tra colonne
        grid.setVgap(10);                          // Spazio verticale tra righe
        grid.setPadding(new Insets(20));           // Margine interno

        // Aggiunge ciascuna label e rispettivo campo: (colonna, riga)
        grid.add(new Label("Nome Menu:"),         0, 0);
        grid.add(nameField,                       1, 0);
        grid.add(new Label("Descrizione:"),       0, 1);
        grid.add(descriptionArea,                 1, 1);
        grid.add(new Label("Prezzo (€):"),        0, 2);
        grid.add(priceField,                      1, 2);

        // Imposta il contenuto del dialog e i pulsanti di conferma e annulla
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Definisce come convertire il risultato del dialog in un oggetto Menu
        setResultConverter(this::processResult);
    }

    /**
     * Crea e inizializza i controlli del form.
     * Se isEdit==true, popola i campi con i valori correnti dell'oggetto menu.
     * Altrimenti lascia i campi vuoti per la creazione.
     *
     * @param isEdit indica se il dialog è in modalità modifica
     */
    private void createFormFields(boolean isEdit) {
        nameField = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);        // Altezza di 3 righe visibili
        priceField = new TextField();

        if (isEdit) {
            // Popola i campi con i dati esistenti del menu
            nameField.setText(menu.getName());
            descriptionArea.setText(menu.getDescription());
            priceField.setText(String.valueOf(menu.getPrice()));
        }
    }

    /**
     * Callback invocato quando l'utente preme OK o CANCEL.
     * Se OK, legge e valida i dati, poi crea o aggiorna il Menu tramite controller.
     *
     * @param buttonType tipo di pulsante premuto (OK o CANCEL)
     * @return oggetto Menu creato/aggiornato o null in caso di annullamento o errore
     */
    private Menu processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                // Lettura e pulizia dei valori inseriti
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                // Validazioni di base
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome del menu non può essere vuoto.");
                    return null;
                }
                if (price < 0) {
                    AlertUtils.showError("Il prezzo non può essere negativo.");
                    return null;
                }

                // Se menu==null, creazione; altrimenti modifica in place
                if (menu == null) {
                    return controller.createMenu(name, description, price);
                } else {
                    menu.setName(name);
                    menu.setDescription(description);
                    menu.setPrice(price);
                    controller.updateMenu(menu);
                    return menu;
                }

            } catch (NumberFormatException e) {
                // Gestione input non numerico per il prezzo
                AlertUtils.showError("Il prezzo deve essere un numero valido.");
            } catch (Exception e) {
                // Qualsiasi altra eccezione in fase di persistenza
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        // Ritorna null per CANCEL o validazione fallita
        return null;
    }
}
