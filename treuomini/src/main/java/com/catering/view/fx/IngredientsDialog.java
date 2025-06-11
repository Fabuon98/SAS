// IngredientsDialog.java - Dialog JavaFX per gestire gli ingredienti di una ricetta
package com.catering.view.fx;

import com.catering.model.Recipe;                   // Model Recipe contenente lista di ingredienti

import javafx.geometry.Insets;                     // Per padding nei layout
import javafx.scene.control.Button;                     // Dialog, Label, ListView, TextField, Button, etc.
import javafx.scene.control.ButtonType;                   // Contenitore orizzontale
import javafx.scene.control.Dialog;                   // Contenitore verticale
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * IngredientsDialog è un dialog personalizzato che permette di:
 *  - visualizzare la lista attuale di ingredienti di una Recipe
 *  - aggiungere nuovi ingredienti
 *  - rimuovere ingredienti esistenti
 *
 * Il dialog non ritorna un valore (Dialog<Void>), ma modifica direttamente
 * la Recipe fornita in constructor.
 */
public class IngredientsDialog extends Dialog<Void> {
    private final Recipe recipe;               // Ricetta di cui gestire gli ingredienti
    private ListView<String> ingredientsList;  // ListView per mostrare gli ingredienti correnti
    private TextField newIngredientField;      // Campo di testo per inserire un nuovo ingrediente

    /**
     * Costruttore.
     * @param recipe la Recipe di cui gestire la lista di ingredienti
     */
    public IngredientsDialog(Recipe recipe) {
        this.recipe = recipe;
        setupDialog();  // Configura titolo, contenuto e bottoni
    }

    /**
     * Configura il dialog:
     *  - titolo e header
     *  - contenuto con VBox: label, ListView e HBox per input e bottoni
     *  - pulsante CLOSE per chiusura
     */
    private void setupDialog() {
        setTitle("Gestione Ingredienti");
        setHeaderText("Ingredienti per: " + recipe.getName());

        // Contenitore principale con spaziatura e padding
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // === ListView degli ingredienti ===
        ingredientsList = new ListView<>();
        ingredientsList.getItems().addAll(recipe.getIngredients());
        ingredientsList.setPrefHeight(200);  // Altezza prefissata per scroll

        // === Campo per nuovo ingrediente ===
        newIngredientField = new TextField();
        newIngredientField.setPromptText("Nuovo ingrediente...");

        // === Pulsanti di azione ===
        Button addIngredientBtn    = new Button("Aggiungi");
        Button removeIngredientBtn = new Button("Rimuovi Selezionato");

        // Disposizione orizzontale di campo + bottoni
        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(newIngredientField, addIngredientBtn, removeIngredientBtn);

        // Associa eventi ai bottoni e campo:
        // - aggiungi ingrediente al click o premendo Enter nel TextField
        // - rimuovi ingrediente selezionato
        addIngredientBtn.setOnAction(e -> addIngredient());
        removeIngredientBtn.setOnAction(e -> removeSelectedIngredient());
        newIngredientField.setOnAction(e -> addIngredient());

        // Aggiunge label, ListView e HBox al VBox principale
        content.getChildren().addAll(
            new Label("Lista Ingredienti:"),
            ingredientsList,
            inputBox
        );

        // Imposta il contenuto del dialog e il pulsante CLOSE
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }

    /**
     * Aggiunge un nuovo ingrediente:
     *  - legge il testo dal TextField
     *  - verifica che non sia vuoto
     *  - verifica che non sia già presente nella lista
     *  - aggiorna sia il model (Recipe) sia la ListView
     *  - altrimenti mostra warning tramite AlertUtils
     */
    private void addIngredient() {
        String newIngredient = newIngredientField.getText().trim();
        if (newIngredient.isEmpty()) {
            AlertUtils.showWarning("Inserisci il nome dell'ingrediente.");
            return;
        }
        // Controllo di duplicato
        if (recipe.getIngredients().contains(newIngredient)) {
            AlertUtils.showWarning("Questo ingrediente è già presente nella lista.");
            return;
        }
        // Aggiunge al model e alla view
        recipe.addIngredient(newIngredient);
        ingredientsList.getItems().add(newIngredient);
        newIngredientField.clear();  // Pulisce il campo per il prossimo inserimento
    }

    /**
     * Rimuove l'ingrediente selezionato:
     *  - controlla che sia selezionato un elemento
     *  - chiede conferma all'utente tramite AlertUtils.confirm(...)
     *  - rimuove sia dal model sia dalla ListView
     *  - se non c'è selezione, mostra warning
     */
    private void removeSelectedIngredient() {
        String selected = ingredientsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un ingrediente da rimuovere.");
            return;
        }
        boolean confirm = AlertUtils.confirm(
            "Conferma Rimozione",
            "Rimuovere ingrediente?",
            "Sei sicuro di voler rimuovere \"" + selected + "\"?"
        );
        if (confirm) {
            recipe.getIngredients().remove(selected);
            ingredientsList.getItems().remove(selected);
        }
    }
}
