// RecipesTabView.java - Gestione della tab “Ricette” nell’interfaccia JavaFX
// Posizione: src/main/java/com/catering/view/fx/RecipesTabView.java

package com.catering.view.fx;

import com.catering.controller.ChefController;  // Controller per operazioni CRUD su Ricette
import com.catering.model.Recipe;              // Modello di dominio per Ricetta

import javafx.geometry.Insets;                 // Per padding nei layout
import javafx.geometry.Pos;                    // Per allineamento nei layout
import javafx.scene.control.Button;                 // Label, Button, ListView, Tab, Alert, ecc.
import javafx.scene.control.Label;               // Layout orizzontale
import javafx.scene.control.ListView;               // Layout verticale
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Classe responsabile della creazione e gestione della Tab “Ricette”.
 * Permette di:
 * - visualizzare l’elenco delle ricette
 * - aggiungere, modificare, eliminare ricette
 * - visualizzare i dettagli (descrizione, tempi, ingredienti, preparazioni)
 * - aprire dialog per gestire ingredienti e preparazioni
 */
public class RecipesTabView {
    private final ChefController controller;    // Controller per logica di business
    private final ListView<Recipe> recipeListView; // Vista elenco ricette

    /**
     * Costruttore:
     * @param controller istanza di ChefController per recuperare e modificare i dati delle ricette
     */
    public RecipesTabView(ChefController controller) {
        this.controller = controller;
        this.recipeListView = new ListView<>();
    }

    /**
     * Crea e restituisce la Tab “Ricette”, non chiudibile dall’utente.
     * @return Tab configurata con contenuto grafico
     */
    public Tab createTab() {
        Tab tab = new Tab("Ricette");
        tab.setClosable(false);             // Disabilita pulsante di chiusura
        tab.setContent(createContent());    // Imposta il contenuto prodotto da createContent()
        return tab;
    }

    /**
     * Costruisce il layout principale della tab:
     * - Titolo
     * - ListView delle ricette
     * - Due file di pulsanti per le azioni CRUD e di gestione dettagli
     * @return VBox contenente tutti i nodi
     */
    private VBox createContent() {
        VBox content = new VBox(10);                // Spaziatura 10px tra i nodi
        content.setPadding(new Insets(10));         // Padding interno 10px

        // Label di intestazione
        Label title = new Label("Gestione Ricette");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Imposta altezza preferita per la ListView
        recipeListView.setPrefHeight(300);

        // Prima riga di pulsanti per CRUD di base
        HBox firstRowButtons = new HBox(10);
        firstRowButtons.setAlignment(Pos.CENTER);

        Button addRecipeBtn    = new Button("Aggiungi Ricetta");
        Button editRecipeBtn   = new Button("Modifica Ricetta");
        Button deleteRecipeBtn = new Button("Elimina Ricetta");

        firstRowButtons.getChildren().addAll(
            addRecipeBtn, editRecipeBtn, deleteRecipeBtn
        );

        // Seconda riga di pulsanti per visualizzazione e gestione dettagli
        HBox secondRowButtons = new HBox(10);
        secondRowButtons.setAlignment(Pos.CENTER);

        Button viewRecipeBtn         = new Button("Visualizza Ricetta");
        Button editIngredientsBtn    = new Button("Gestisci Ingredienti");
        Button editPreparationsBtn   = new Button("Gestisci Preparazioni");

        secondRowButtons.getChildren().addAll(
            viewRecipeBtn, editIngredientsBtn, editPreparationsBtn
        );

        // Assegnazione degli handler agli eventi di click sui pulsanti
        addRecipeBtn.setOnAction(e -> showAddRecipeDialog());
        editRecipeBtn.setOnAction(e -> showEditRecipeDialog());
        deleteRecipeBtn.setOnAction(e -> deleteSelectedRecipe());
        viewRecipeBtn.setOnAction(e -> showRecipeDetails());
        editIngredientsBtn.setOnAction(e -> showIngredientsDialog());
        editPreparationsBtn.setOnAction(e -> showPreparationsDialog());

        // Composizione finale del VBox
        content.getChildren().addAll(
            title,
            recipeListView,
            firstRowButtons,
            secondRowButtons
        );
        return content;
    }

    /**
     * Apre il dialog per aggiungere una nuova ricetta.
     * Se confermato, ricarica la lista e mostra un messaggio di successo.
     */
    private void showAddRecipeDialog() {
        RecipeDialog dialog = new RecipeDialog(controller, null);
        dialog.showAndWait().ifPresent(recipe -> {
            refreshData();
            AlertUtils.showSuccess("Ricetta creata con successo!");
        });
    }

    /**
     * Apre il dialog per modificare la ricetta selezionata.
     * Se nessuna è selezionata, mostra un warning.
     * Se confermato, ricarica la lista e mostra un messaggio di successo.
     */
    private void showEditRecipeDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da modificare.");
            return;
        }
        RecipeDialog dialog = new RecipeDialog(controller, selected);
        dialog.showAndWait().ifPresent(recipe -> {
            refreshData();
            AlertUtils.showSuccess("Ricetta modificata con successo!");
        });
    }

    /**
     * Elimina la ricetta selezionata dopo conferma.
     * Se nessuna è selezionata, mostra un warning.
     * Se confermato, chiama il controller, ricarica la lista e mostra un messaggio di successo.
     */
    private void deleteSelectedRecipe() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da eliminare.");
            return;
        }
        if (AlertUtils.confirmDelete("ricetta", selected.getName())) {
            controller.deleteRecipe(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Ricetta eliminata con successo!");
        }
    }

    /**
     * Mostra i dettagli completi della ricetta selezionata (descrizione, tempi, porzioni, difficoltà,
     * numero di ingredienti e preparazioni) in un Alert informativo.
     * Se nessuna è selezionata, mostra un warning.
     */
    private void showRecipeDetails() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta da visualizzare.");
            return;
        }

        // Costruisce il contenuto testuale dei dettagli
        StringBuilder content = new StringBuilder();
        content.append("Descrizione: ").append(selected.getDescription()).append("\n");
        content.append("Tempo preparazione: ").append(selected.getPreparationTime()).append(" min\n");
        content.append("Tempo cottura: ").append(selected.getCookingTime()).append(" min\n");
        content.append("Porzioni: ").append(selected.getServings()).append("\n");
        content.append("Difficoltà: ").append(selected.getDifficulty()).append("\n");
        content.append("Ingredienti: ").append(selected.getIngredients().size()).append("\n");
        content.append("Preparazioni: ").append(selected.getPreparations().size());

        AlertUtils.showInfo(
            "Dettagli Ricetta",
            "Ricetta: " + selected.getName(),
            content.toString()
        );
    }

    /**
     * Apre il dialog per gestire (aggiungere/rimuovere) gli ingredienti della ricetta selezionata.
     * Se nessuna è selezionata, mostra un warning.
     */
    private void showIngredientsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta per gestire gli ingredienti.");
            return;
        }
        IngredientsDialog dialog = new IngredientsDialog(selected);
        dialog.showAndWait();
    }

    /**
     * Apre il dialog per gestire (aggiungere/modificare/rimuovere) i passaggi di preparazione
     * della ricetta selezionata. Se nessuna è selezionata, mostra un warning.
     */
    private void showPreparationsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una ricetta per gestire le preparazioni.");
            return;
        }
        PreparationsDialog dialog = new PreparationsDialog(controller, selected);
        dialog.showAndWait();
    }

    /**
     * Ricarica tutte le ricette dal controller:
     * - Pulisce la ListView
     * - Aggiunge tutti gli elementi restituiti da controller.getAllRecipes()
     */
    public void refreshData() {
        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(controller.getAllRecipes());
    }
}