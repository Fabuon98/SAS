// SectionDialog.java - Dialog per creare o modificare una Sezione di menu
// Posizione: src/main/java/com/catering/view/fx/SectionDialog.java

package com.catering.view.fx;

import com.catering.controller.ChefController;  // Controller per operazioni CRUD su Section
import com.catering.model.Section;             // Modello di dominio per Sezione

import javafx.geometry.Insets;                  // Per impostare i margini del GridPane
import javafx.scene.control.ButtonType;                  // Dialog, TextField, TextArea, ButtonType, Label, Alert
import javafx.scene.control.Dialog;            // Layout a griglia per organizzare i campi
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Dialog personalizzato per gestire la creazione e la modifica di oggetti Section.
 * Estende javafx.scene.control.Dialog parametrizzato su Section:
 *  - restituisce una Section se l'utente conferma (OK)
 *  - restituisce null se l'utente annulla o in caso di errori di validazione
 */
public class SectionDialog extends Dialog<Section> {
    private final ChefController controller; // Controller per salvare o aggiornare la Section
    private final Section section;          // Sezione esistente (edit) o null (create)

    // Campi del form
    private TextField nameField;            // Input per il nome della sezione
    private TextArea descriptionArea;       // Input per la descrizione della sezione

    /**
     * Costruttore:
     * @param controller istanza di ChefController per chiamate a create/update
     * @param section    sezione esistente da modificare, o null per una nuova sezione
     */
    public SectionDialog(ChefController controller, Section section) {
        this.controller = controller;
        this.section = section;
        setupDialog();  // Configura titolo, contenuto, bottoni e converter
    }

    /**
     * Configura la finestra di dialog:
     *  - Titolo e header in base a create vs edit
     *  - Campi del form
     *  - Layout GridPane con padding e gap
     *  - Bottoni OK/Cancel
     *  - ResultConverter per processare l'esito
     */
    private void setupDialog() {
        boolean isEdit = (section != null); // True se stiamo modificando

        // Titolo e testo header dinamici
        setTitle(isEdit ? "Modifica Sezione" : "Aggiungi Nuova Sezione");
        setHeaderText(isEdit 
            ? "Modifica i dettagli della sezione" 
            : "Inserisci i dettagli della nuova sezione"
        );

        // Crea e configura i campi di input
        createFormFields(isEdit);

        // Costruzione del layout a griglia
        GridPane grid = new GridPane();
        grid.setHgap(10);                   // Spazio orizzontale tra colonne
        grid.setVgap(10);                   // Spazio verticale tra righe
        grid.setPadding(new Insets(20));    // Margine interno di 20px su tutti i lati

        // Aggiunge etichette e campi al GridPane
        grid.add(new Label("Nome Sezione:"),   0, 0);
        grid.add(nameField,                     1, 0);
        grid.add(new Label("Descrizione:"),    0, 1);
        grid.add(descriptionArea,               1, 1);

        // Imposta il contenuto del DialogPane
        getDialogPane().setContent(grid);

        // Aggiunge i bottoni di conferma e annulla
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Configura il converter per trasformare il ButtonType in un oggetto Section
        setResultConverter(this::processResult);
    }

    /**
     * Inizializza i campi del form.
     * Se isEdit==true, popola i campi con i valori della Section esistente.
     * @param isEdit indica se stiamo modificando un oggetto già esistente
     */
    private void createFormFields(boolean isEdit) {
        nameField       = new TextField();
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);  // Altezza di 3 righe per il TextArea

        if (isEdit) {
            // Precompila i campi con i dati correnti della Section
            nameField.setText(section.getName());
            descriptionArea.setText(section.getDescription());
        }
    }

    /**
     * Converte il ButtonType selezionato dall'utente in un oggetto Section.
     * - Se OK: valida i campi, crea o aggiorna la Section via controller
     * - Se CANCEL o validazione fallita: restituisce null
     * @param buttonType tipo di bottone premuto (OK o CANCEL)
     * @return nuova Section creata/aggiornata oppure null
     */
    private Section processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                // Estrae e trimma i valori inseriti
                String name        = nameField.getText().trim();
                String description = descriptionArea.getText().trim();

                // Validazione minima: il nome non può essere vuoto
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome della sezione non può essere vuoto.");
                    return null;
                }

                // Sezione nuova vs esistente
                if (section == null) {
                    // Creazione di una nuova Section tramite controller
                    return controller.createSection(name, description);
                } else {
                    // Aggiorna i campi della Section esistente e la salva
                    section.setName(name);
                    section.setDescription(description);
                    controller.updateSection(section);
                    return section;
                }

            } catch (Exception e) {
                // In caso di eccezioni mostra un alert con il messaggio di errore
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        // Se CANCEL o validazione fallita, ritorna null facendo annullare l'azione
        return null;
    }
}