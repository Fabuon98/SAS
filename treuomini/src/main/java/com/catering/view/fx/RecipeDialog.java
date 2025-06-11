// RecipeDialog.java - Dialog JavaFX per gestire la creazione e modifica delle ricette
package com.catering.view.fx;

import com.catering.controller.ChefController; // Controller per operazioni CRUD su Recipe
import com.catering.model.Recipe;             // Model della ricetta

import javafx.geometry.Insets;                // Utilizzato per i padding nel layout
import javafx.scene.control.ButtonType;                // Dialog, TextField, TextArea, ComboBox, ButtonType, Alert
import javafx.scene.control.ComboBox;          // Layout a griglia per posizionare i campi
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Dialog personalizzato per inserire o modificare una Recipe.
 * <ul>
 *   <li>Se il costruttore riceve recipe == null, il dialog crea una nuova ricetta.</li>
 *   <li>Altrimenti, popola i campi con i dati esistenti per la modifica.</li>
 * </ul>
 * Il risultato (Recipe) viene restituito solo se l'utente conferma con OK.
 */
public class RecipeDialog extends Dialog<Recipe> {
    // ===== Riferimenti al Controller e al Model =====
    private final ChefController controller;   // Per invocare metodi di creazione/aggiornamento
    private final Recipe recipe;               // Se null => nuova ricetta; altrimenti => modifica

    // ===== Controlli del form =====
    private TextField nameField;               // Campo per il nome della ricetta
    private TextArea descriptionArea;          // Area di testo per la descrizione
    private TextField prepTimeField;           // Tempo di preparazione (minuti)
    private TextField cookTimeField;           // Tempo di cottura (minuti)
    private TextField servingsField;           // Numero di porzioni
    private ComboBox<String> difficultyCombo;  // Combo per selezionare la difficoltà

    /**
     * Costruttore.
     * @param controller il controller che gestisce la logica di business
     * @param recipe     l'oggetto Recipe da modificare, oppure null per crearne uno nuovo
     */
    public RecipeDialog(ChefController controller, Recipe recipe) {
        this.controller = controller;
        this.recipe = recipe;
        setupDialog(); // Configura titolo, layout, campi e convertitore del risultato
    }

    /**
     * Configura l'interfaccia del Dialog:
     * - titolo e header in base alla modalità (creazione/modifica)
     * - genera i campi del form e li popola se necessario
     * - definisce il layout a griglia
     * - aggiunge i pulsanti OK e CANCEL
     * - imposta il converter per trasformare l'input in un oggetto Recipe
     */
    private void setupDialog() {
        boolean isEdit = (recipe != null);

        // Titolo e testo di intestazione
        setTitle(isEdit ? "Modifica Ricetta" : "Aggiungi Nuova Ricetta");
        setHeaderText(isEdit
            ? "Modifica i dettagli della ricetta"
            : "Inserisci i dettagli della nuova ricetta"
        );

        // Inizializza i controlli del form, con eventuale popolamento iniziale
        createFormFields(isEdit);

        // Layout a griglia: due colonne (label + campo), 6 righe
        GridPane grid = new GridPane();
        grid.setHgap(10);               // Spaziatura orizzontale tra colonne
        grid.setVgap(10);               // Spaziatura verticale tra righe
        grid.setPadding(new Insets(20));// Padding interno al GridPane

        // Aggiunta di label e controlli (colonna, riga)
        grid.add(new Label("Nome Ricetta:"),      0, 0);
        grid.add(nameField,                       1, 0);

        grid.add(new Label("Descrizione:"),       0, 1);
        grid.add(descriptionArea,                 1, 1);

        grid.add(new Label("Tempo prep. (min):"), 0, 2);
        grid.add(prepTimeField,                   1, 2);

        grid.add(new Label("Tempo cottura (min):"), 0, 3);
        grid.add(cookTimeField,                     1, 3);

        grid.add(new Label("Porzioni:"),           0, 4);
        grid.add(servingsField,                    1, 4);

        grid.add(new Label("Difficoltà:"),         0, 5);
        grid.add(difficultyCombo,                  1, 5);

        // Imposta il contenuto del dialog
        getDialogPane().setContent(grid);
        // Aggiunge i pulsanti di conferma e cancellazione
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Definisce come convertire l'input in un oggetto Recipe
        setResultConverter(this::processResult);
    }

    /**
     * Crea e configura i campi del form.
     * Se isEdit==true, popola con i valori della ricetta esistente.
     * Altrimenti, imposta valori di default/minimi.
     *
     * @param isEdit true se si sta modificando una ricetta esistente
     */
    private void createFormFields(boolean isEdit) {
        // Campo per il nome (obbligatorio)
        nameField = new TextField();

        // Area per la descrizione (opzionale)
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(2);

        // Campi numerici come TextField (conversione manuale + validazione)
        prepTimeField   = new TextField();
        cookTimeField   = new TextField();
        servingsField   = new TextField();

        // ComboBox per il livello di difficoltà
        difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("Facile", "Medio", "Difficile");
        difficultyCombo.setValue("Facile"); // Default

        if (isEdit) {
            // Popola i campi con i valori dell'oggetto Recipe
            nameField.setText(recipe.getName());
            descriptionArea.setText(recipe.getDescription());
            prepTimeField.setText(String.valueOf(recipe.getPreparationTime()));
            cookTimeField.setText(String.valueOf(recipe.getCookingTime()));
            servingsField.setText(String.valueOf(recipe.getServings()));
            // Se difficulty è null, lascia il default "Facile"
            String diff = recipe.getDifficulty();
            difficultyCombo.setValue(diff != null ? diff : difficultyCombo.getValue());
        }
    }

    /**
     * Callback invocato quando l'utente sceglie OK o CANCEL.
     * Se OK, legge e valida i campi, crea o aggiorna la Recipe,
     * altrimenti restituisce null.
     *
     * @param buttonType tipo di pulsante premuto
     * @return Recipe creata o modificata, o null
     */
    private Recipe processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            try {
                // Lettura e trim dei campi testuali
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();

                // Validazione obbligo nome
                if (name.isEmpty()) {
                    AlertUtils.showError("Il nome della ricetta non può essere vuoto.");
                    return null;
                }

                // Conversione e validazione dei campi numerici
                int prepTime = 0, cookTime = 0, servings = 0;

                // Tempo preparazione (se fornito)
                String prepTxt = prepTimeField.getText().trim();
                if (!prepTxt.isEmpty()) {
                    prepTime = Integer.parseInt(prepTxt);
                    if (prepTime < 0) {
                        AlertUtils.showError("Il tempo di preparazione non può essere negativo.");
                        return null;
                    }
                }

                // Tempo cottura (se fornito)
                String cookTxt = cookTimeField.getText().trim();
                if (!cookTxt.isEmpty()) {
                    cookTime = Integer.parseInt(cookTxt);
                    if (cookTime < 0) {
                        AlertUtils.showError("Il tempo di cottura non può essere negativo.");
                        return null;
                    }
                }

                // Porzioni (se fornito)
                String servTxt = servingsField.getText().trim();
                if (!servTxt.isEmpty()) {
                    servings = Integer.parseInt(servTxt);
                    if (servings < 0) {
                        AlertUtils.showError("Il numero di porzioni non può essere negativo.");
                        return null;
                    }
                }

                // Livello di difficoltà selezionato
                String difficulty = difficultyCombo.getValue();

                // Creazione o aggiornamento del model
                if (recipe == null) {
                    // Nuova ricetta: chiama controller e imposta i campi aggiuntivi
                    Recipe newRecipe = controller.createRecipe(name, description);
                    newRecipe.setPreparationTime(prepTime);
                    newRecipe.setCookingTime(cookTime);
                    newRecipe.setServings(servings);
                    newRecipe.setDifficulty(difficulty);
                    return newRecipe;
                } else {
                    // Modifica esistente: aggiorna il model e chiama update
                    recipe.setName(name);
                    recipe.setDescription(description);
                    recipe.setPreparationTime(prepTime);
                    recipe.setCookingTime(cookTime);
                    recipe.setServings(servings);
                    recipe.setDifficulty(difficulty);
                    controller.updateRecipe(recipe);
                    return recipe;
                }
            } catch (NumberFormatException e) {
                // Errore di parsing numerico
                AlertUtils.showError("I campi numerici devono contenere numeri validi.");
            } catch (Exception e) {
                // Qualsiasi altra eccezione
                AlertUtils.showError("Errore durante il salvataggio: " + e.getMessage());
            }
        }
        // Se l'utente preme CANCEL o se validation fallisce
        return null;
    }
}
