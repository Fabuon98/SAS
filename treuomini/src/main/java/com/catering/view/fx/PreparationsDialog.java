// PreparationsDialog.java - Dialog JavaFX per gestire i passaggi di preparazione di una Recipe
package com.catering.view.fx;

import com.catering.controller.ChefController;  // Controller per operazioni su Preparations
import com.catering.model.Preparation;               // Model della ricetta
import com.catering.model.Recipe;          // Model di uno step di preparazione

import javafx.geometry.Insets;                  // Padding nei layout
import javafx.scene.control.Button;                  // Dialog, Label, ListView, TextField, Button, etc.
import javafx.scene.control.ButtonType;            // Layout a griglia per il form
import javafx.scene.control.Dialog;                // Contenitore orizzontale per bottoni
import javafx.scene.control.Label;                // Contenitore verticale per l'intero dialog
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Dialog per visualizzare, aggiungere, modificare e rimuovere gli oggetti Preparation
 * associati a una specifica Recipe.
 * Estende Dialog<Void> poiché i cambiamenti vengono applicati direttamente al model.
 */
public class PreparationsDialog extends Dialog<Void> {
    private final ChefController controller;     // Controller per creare nuove Preparation
    private final Recipe recipe;                 // Ricetta di cui gestire le preparazioni
    private ListView<Preparation> preparationsList;  // ListView che mostra tutti gli step

    // Campi del form per inserire o modificare uno step
    private TextField stepField;                 // Numero dello step
    private TextArea descriptionArea;            // Descrizione del passo
    private TextField timeField;                 // Tempo in minuti per il passo

    /**
     * Costruttore.
     * @param controller il controller condiviso
     * @param recipe     la ricetta di cui gestire le preparazioni
     */
    public PreparationsDialog(ChefController controller, Recipe recipe) {
        this.controller = controller;
        this.recipe = recipe;
        setupDialog();                            // Configura titolo, contenuto e bottoni
    }

    /**
     * Configura il dialog:
     * - titolo e header
     * - VBox principale con ListView, form e bottoni
     * - pulsante CLOSE
     */
    private void setupDialog() {
        setTitle("Gestione Preparazioni");
        setHeaderText("Preparazioni per: " + recipe.getName());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // ===== ListView delle preparazioni esistenti =====
        preparationsList = new ListView<>();
        preparationsList.getItems().addAll(recipe.getPreparations());
        preparationsList.setPrefHeight(200);      // Altezza fissa per lo scrolling

        // ===== Form per aggiungere/modificare uno step =====
        GridPane prepForm = createPreparationForm();

        // ===== Bottoni di azione =====
        HBox buttonBox = new HBox(10);
        Button addPrepBtn    = new Button("Aggiungi Preparazione");
        Button removePrepBtn = new Button("Rimuovi Selezionata");
        Button editPrepBtn   = new Button("Modifica Selezionata");
        Button clearFormBtn  = new Button("Pulisci Form");
        buttonBox.getChildren().addAll(addPrepBtn, removePrepBtn, editPrepBtn, clearFormBtn);

        // Associa le azioni ai pulsanti
        addPrepBtn.setOnAction(e -> addPreparation());
        removePrepBtn.setOnAction(e -> removeSelectedPreparation());
        editPrepBtn.setOnAction(e -> editSelectedPreparation());
        clearFormBtn.setOnAction(e -> clearForm());

        // Combina tutto nel VBox principale
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

    /**
     * Crea il form (GridPane) con i campi:
     * stepField, descriptionArea, timeField.
     * @return il GridPane pronto all'uso
     */
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

        // Posiziona label e campi: (col, row)
        prepForm.add(new Label("Step:"),            0, 0);
        prepForm.add(stepField,                     1, 0);
        prepForm.add(new Label("Descrizione:"),     0, 1);
        prepForm.add(descriptionArea,               1, 1);
        prepForm.add(new Label("Tempo (min):"),     0, 2);
        prepForm.add(timeField,                     1, 2);

        return prepForm;
    }

    /**
     * Legge i valori dai campi, valida input e aggiunge una nuova Preparation.
     * Gestisce errori di formato e duplicati di stepNumber.
     */
    private void addPreparation() {
        try {
            String stepText = stepField.getText().trim();
            String description = descriptionArea.getText().trim();
            String timeText = timeField.getText().trim();

            // Validazioni base sui campi
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

            // Verifica duplicato di stepNumber
            boolean stepExists = recipe.getPreparations().stream()
                .anyMatch(prep -> prep.getStepNumber() == step);
            if (stepExists) {
                AlertUtils.showError("Esiste già una preparazione con step numero " + step + ".");
                return;
            }

            // Crea lo step tramite controller e lo aggiunge al model + view
            Preparation prep = controller.createPreparation(step, description, time);
            recipe.addPreparation(prep);
            preparationsList.getItems().add(prep);

            clearForm();               // Pulisce i campi
            sortPreparationsList();    // Ordina la ListView per stepNumber
            AlertUtils.showSuccess("Preparazione aggiunta con successo!");

        } catch (NumberFormatException e) {
            AlertUtils.showError("Step e tempo devono essere numeri validi.");
        } catch (Exception e) {
            AlertUtils.showError("Errore durante l'aggiunta: " + e.getMessage());
        }
    }

    /**
     * Rimuove lo step selezionato dopo conferma.
     * Aggiorna sia il model che la ListView.
     */
    private void removeSelectedPreparation() {
        Preparation selected = preparationsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una preparazione da rimuovere.");
            return;
        }
        boolean confirmed = AlertUtils.confirm(
            "Conferma Rimozione",
            "Rimuovere preparazione?",
            "Sei sicuro di voler rimuovere lo step " 
                + selected.getStepNumber() + ": \"" 
                + selected.getDescription() + "\"?"
        );
        if (confirmed) {
            recipe.getPreparations().remove(selected);
            preparationsList.getItems().remove(selected);
            AlertUtils.showSuccess("Preparazione rimossa con successo!");
        }
    }

    /**
     * Carica i dati dello step selezionato nei campi per la modifica:
     * rimuove temporaneamente lo step da model + view per poi riaggiungerlo.
     */
    private void editSelectedPreparation() {
        Preparation selected = preparationsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una preparazione da modificare.");
            return;
        }
        // Popola form con i dati esistenti
        stepField.setText(String.valueOf(selected.getStepNumber()));
        descriptionArea.setText(selected.getDescription());
        timeField.setText(String.valueOf(selected.getTimeInMinutes()));

        // Rimuove lo step per evitare duplicati di stepNumber
        recipe.getPreparations().remove(selected);
        preparationsList.getItems().remove(selected);

        AlertUtils.showInfo(
            "Modifica Preparazione",
            "Preparazione caricata",
            "Modifica i campi e clicca 'Aggiungi Preparazione' per salvare le modifiche."
        );
    }

    /** Svuota tutti i campi del form. */
    private void clearForm() {
        stepField.clear();
        descriptionArea.clear();
        timeField.clear();
    }

    /** Ordina la ListView delle preparazioni per numero dello step. */
    private void sortPreparationsList() {
        preparationsList.getItems().sort(
            (p1, p2) -> Integer.compare(p1.getStepNumber(), p2.getStepNumber())
        );
    }

}
