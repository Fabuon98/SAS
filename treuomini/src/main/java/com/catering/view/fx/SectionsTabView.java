// SectionsTabView.java - Gestione della tab “Sezioni” nell’interfaccia JavaFX
// Posizione: src/main/java/com/catering/view/fx/SectionsTabView.java

package com.catering.view.fx;

import com.catering.controller.ChefController;   // Controller per operazioni CRUD su sezioni e ricette
import com.catering.model.Recipe;              // Modello di dominio per Sezione di menu
import com.catering.model.Section;               // Modello di dominio per Ricetta

import javafx.geometry.Insets;                  // Per impostare padding nei layout
import javafx.geometry.Pos;                     // Per l’allineamento nei layout
import javafx.scene.control.Button;                  // Import di Label, Button, ListView, Tab, Alert, ecc.
import javafx.scene.control.Label;                // Layout orizzontale per i pulsanti
import javafx.scene.control.ListView;                // Layout verticale per titolo, lista e pulsanti
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Classe responsabile della creazione e gestione della Tab “Sezioni”
 * all’interno dell’applicazione JavaFX.
 * Mostra un elenco di sezioni, permette operazioni di aggiunta, modifica,
 * eliminazione e visualizzazione delle ricette associate.
 */
public class SectionsTabView {
    private final ChefController controller;         // Controller per logica di business
    private final ListView<Section> sectionListView; // Vista elenco sezioni

    /**
     * Costruttore: riceve il controller e inizializza la ListView.
     * @param controller istanza di ChefController per recupero e modifica dati
     */
    public SectionsTabView(ChefController controller) {
        this.controller = controller;
        this.sectionListView = new ListView<>();
    }

    /**
     * Crea e restituisce la Tab “Sezioni”, non chiudibile.
     * @return oggetto Tab con contenuto generato da createContent()
     */
    public Tab createTab() {
        Tab tab = new Tab("Sezioni");
        tab.setClosable(false);          // Disabilita la chiusura manuale
        tab.setContent(createContent()); // Imposta il contenuto principale
        return tab;
    }

    /**
     * Costruisce il layout verticale principale:
     * - Label di titolo
     * - ListView delle Section
     * - Barra orizzontale di pulsanti per le azioni
     * @return VBox configurato con padding e spaziatura
     */
    private VBox createContent() {
        VBox content = new VBox(10);                // Spaziatura di 10px tra i nodi
        content.setPadding(new Insets(10));         // Padding interno di 10px su tutti i lati

        // Titolo della sezione
        Label title = new Label("Gestione Sezioni Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Altezza preferita per la ListView
        sectionListView.setPrefHeight(300);

        // HBox per i pulsanti, con spaziatura e allineamento centrato
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        // Creazione dei pulsanti per le azioni CRUD e visualizzazione ricette
        Button addSectionBtn         = new Button("Aggiungi Sezione");
        Button editSectionBtn        = new Button("Modifica Sezione");
        Button deleteSectionBtn      = new Button("Elimina Sezione");
        Button viewSectionRecipesBtn = new Button("Visualizza Ricette");

        // Aggiunge tutti i pulsanti all'HBox
        buttons.getChildren().addAll(
            addSectionBtn,
            editSectionBtn,
            deleteSectionBtn,
            viewSectionRecipesBtn
        );

        // Event handler per ciascun pulsante
        addSectionBtn.setOnAction(e -> showAddSectionDialog());
        editSectionBtn.setOnAction(e -> showEditSectionDialog());
        deleteSectionBtn.setOnAction(e -> deleteSelectedSection());
        viewSectionRecipesBtn.setOnAction(e -> showSectionRecipes());

        // Compone il VBox con titolo, lista e pulsanti
        content.getChildren().addAll(title, sectionListView, buttons);
        return content;
    }

    /**
     * Apre un dialog per aggiungere una nuova sezione.
     * Al conferma, ricarica i dati e mostra un messaggio di successo.
     */
    private void showAddSectionDialog() {
        SectionDialog dialog = new SectionDialog(controller, null);
        dialog.showAndWait().ifPresent(section -> {
            refreshData();  
            AlertUtils.showSuccess("Sezione creata con successo!");
        });
    }

    /**
     * Apre un dialog per modificare la sezione selezionata.
     * Se non è nulla, aggiorna i dati e mostra messaggio di successo;
     * altrimenti mostra un warning.
     */
    private void showEditSectionDialog() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione da modificare.");
            return;
        }
        SectionDialog dialog = new SectionDialog(controller, selected);
        dialog.showAndWait().ifPresent(section -> {
            refreshData();
            AlertUtils.showSuccess("Sezione modificata con successo!");
        });
    }

    /**
     * Elimina la sezione selezionata dopo conferma.
     * Se non è nulla e l’utente conferma, la rimuove e aggiorna la vista.
     */
    private void deleteSelectedSection() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione da eliminare.");
            return;
        }
        if (AlertUtils.confirmDelete("sezione", selected.getName())) {
            controller.deleteSection(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Sezione eliminata con successo!");
        }
    }

    /**
     * Mostra in un Alert le ricette collegate alla sezione selezionata.
     * Se non ne esistono, informa l’utente; altrimenti elenca nome, difficoltà e tempo.
     */
    private void showSectionRecipes() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona una sezione per visualizzare le sue ricette.");
            return;
        }

        StringBuilder content = new StringBuilder();
        if (selected.getRecipes().isEmpty()) {
            content.append("Nessuna ricetta collegata a questa sezione.");
        } else {
            content.append("Ricette collegate:\n\n");
            for (Recipe recipe : selected.getRecipes()) {
                content.append("• ").append(recipe.getName()).append("\n");
                if (recipe.getDifficulty() != null) {
                    content.append("  Difficoltà: ").append(recipe.getDifficulty()).append("\n");
                }
                if (recipe.getPreparationTime() > 0) {
                    content.append("  Tempo preparazione: ")
                           .append(recipe.getPreparationTime())
                           .append(" min\n");
                }
                content.append("\n");
            }
        }

        AlertUtils.showInfo(
            "Ricette della Sezione",
            "Ricette della sezione: " + selected.getName(),
            content.toString()
        );
    }

    /**
     * Ricarica tutte le sezioni dal controller cancellando i vecchi elementi
     * e popolando la ListView con quelli correnti.
     */
    public void refreshData() {
        sectionListView.getItems().clear();
        sectionListView.getItems().addAll(controller.getAllSections());
    }
}