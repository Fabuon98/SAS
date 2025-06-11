// MenusTabView.java - Gestione della tab "Menu"
package com.catering.view.fx;

import com.catering.controller.ChefController;     // Controller per operazioni CRUD su Menu
import com.catering.model.Menu;                   // Model del Menu
import com.catering.model.Section;                // Model della Section (per i dettagli)

import javafx.geometry.Insets;                    // Per padding nei layout
import javafx.geometry.Pos;                       // Per allineamento nei contenitori
import javafx.scene.control.Button;                    // Label, ListView, Button, Tab, etc.
import javafx.scene.control.Label;                  // Contenitore orizzontale
import javafx.scene.control.ListView;                  // Contenitore verticale
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * La classe MenusTabView crea e gestisce l'interfaccia grafica
 * per la gestione dei menu:
 *  - visualizzare la lista dei menu
 *  - aggiungere un nuovo menu
 *  - modificare un menu esistente
 *  - eliminare un menu
 *  - mostrare i dettagli di un menu selezionato
 */
public class MenusTabView {
    // Riferimento al controller che fornisce i dati e le operazioni CRUD
    private final ChefController controller;
    // ListView che mostra tutti i Menu disponibili
    private final ListView<Menu> menuListView;

    /**
     * Costruttore.
     * Inizializza il riferimento al controller e la ListView vuota.
     *
     * @param controller istanza condivisa di ChefController
     */
    public MenusTabView(ChefController controller) {
        this.controller = controller;
        this.menuListView = new ListView<>();      // Inizializza la ListView per i menu
    }

    /**
     * Crea e configura la Tab "Menu":
     *  - imposta il titolo
     *  - impedisce la chiusura
     *  - associa il contenuto con createContent()
     *
     * @return una Tab configurata per la gestione dei menu
     */
    public Tab createTab() {
        Tab tab = new Tab("Menu");
        tab.setClosable(false);                     // Non permettere la chiusura da parte dell'utente
        tab.setContent(createContent());            // Inserisce il layout nel contenuto della tab
        return tab;
    }

    /**
     * Costruisce il layout principale della scheda:
     *  - un VBox con titolo, ListView e HBox di pulsanti
     *
     * @return un VBox pronto per essere inserito nella Tab
     */
    private VBox createContent() {
        VBox content = new VBox(10);                // Spaziatura verticale di 10px tra i nodi
        content.setPadding(new Insets(10));         // Padding interno di 10px

        // Titolo della sezione
        Label title = new Label("Gestione Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Configura la ListView per mostrare i menu
        menuListView.setPrefHeight(300);            // Altezza preferita per scroll

        // Contenitore orizzontale per i pulsanti di azione
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);           // Allinea i pulsanti orizzontalmente al centro

        // Pulsanti per aggiungere, modificare, eliminare e visualizzare i menu
        Button addMenuBtn    = new Button("Aggiungi Menu");
        Button editMenuBtn   = new Button("Modifica Menu");
        Button deleteMenuBtn = new Button("Elimina Menu");
        Button viewMenuBtn   = new Button("Visualizza Menu");

        // Aggiunge i pulsanti all'HBox
        buttons.getChildren().addAll(
            addMenuBtn, editMenuBtn, deleteMenuBtn, viewMenuBtn
        );

        // Associa gli handler per le azioni dei pulsanti
        addMenuBtn.setOnAction(e -> showAddMenuDialog());
        editMenuBtn.setOnAction(e -> showEditMenuDialog());
        deleteMenuBtn.setOnAction(e -> deleteSelectedMenu());
        viewMenuBtn.setOnAction(e -> showMenuDetails());

        // Aggiunge titolo, lista e pulsanti al VBox principale
        content.getChildren().addAll(title, menuListView, buttons);
        return content;
    }

    /**
     * Mostra un dialog per l'aggiunta di un nuovo menu.
     * Se l'utente conferma, ricarica i dati e mostra un alert di successo.
     */
    private void showAddMenuDialog() {
        MenuDialog dialog = new MenuDialog(controller, null);
        dialog.showAndWait().ifPresent(menu -> {
            refreshData();
            AlertUtils.showSuccess("Menu creato con successo!");
        });
    }

    /**
     * Mostra un dialog per la modifica del menu selezionato.
     * Se non c'è selezione, mostra un warning.
     * Al termine dell'edit, ricarica i dati e mostra un alert di successo.
     */
    private void showEditMenuDialog() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da modificare.");
            return;
        }
        MenuDialog dialog = new MenuDialog(controller, selected);
        dialog.showAndWait().ifPresent(menu -> {
            refreshData();
            AlertUtils.showSuccess("Menu modificato con successo!");
        });
    }

    /**
     * Elimina il menu selezionato dopo conferma da parte dell'utente.
     * Se non c'è selezione, mostra un warning.
     * Al termine, ricarica la lista e mostra un alert di successo.
     */
    private void deleteSelectedMenu() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da eliminare.");
            return;
        }
        if (AlertUtils.confirmDelete("menu", selected.getName())) {
            controller.deleteMenu(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Menu eliminato con successo!");
        }
    }

    /**
     * Mostra i dettagli del menu selezionato in un Alert.
     * Include descrizione, prezzo e lista di sezioni associate.
     * Se non c'è selezione, mostra un warning.
     */
    private void showMenuDetails() {
        Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un menu da visualizzare.");
            return;
        }

        // Costruisce il testo con le informazioni del menu
        StringBuilder content = new StringBuilder();
        content.append("Descrizione: ").append(selected.getDescription()).append("\n");
        content.append("Prezzo: €").append(selected.getPrice()).append("\n");
        content.append("Sezioni: ").append(selected.getSections().size()).append("\n\n");

        // Se ci sono sezioni, elencale con i loro nomi
        if (!selected.getSections().isEmpty()) {
            content.append("Lista Sezioni:\n");
            for (Section section : selected.getSections()) {
                content.append("• ").append(section.getName()).append("\n");
            }
        }

        AlertUtils.showInfo(
            "Dettagli Menu",
            "Dettagli di: " + selected.getName(),
            content.toString()
        );
    }

    /**
     * Ricarica i dati in ListView leggendo tutti i menu dal controller.
     * Da chiamare dopo ogni operazione di creazione, modifica o cancellazione.
     */
    public void refreshData() {
        menuListView.getItems().setAll(controller.getAllMenus());
    }
}
