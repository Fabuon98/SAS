// EventsTabView.java - Gestione della tab "Eventi"
package com.catering.view.fx;

import com.catering.controller.ChefController;    // Controller per operazioni CRUD sugli Event
import com.catering.model.Event;                  // Model dell’evento

import javafx.geometry.Insets;                    // Per padding tra i nodi
import javafx.geometry.Pos;                       // Per allineamenti in HBox
import javafx.scene.control.Button;                    // Label, ListView, Button, Tab, etc.
import javafx.scene.control.Label;                  // Contenitore orizzontale per bottoni
import javafx.scene.control.ListView;                  // Contenitore verticale per layout
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * La classe EventsTabView crea e gestisce l'interfaccia grafica
 * per la gestione degli Eventi: visualizzazione, aggiunta, modifica,
 * eliminazione e dettagli.
 */
public class EventsTabView {
    private final ChefController controller;       // Riferimento al controller centrale
    private final ListView<Event> eventListView;  // ListView per mostrare gli eventi

    /**
     * Costruttore.
     * @param controller istanza condivisa di ChefController
     */
    public EventsTabView(ChefController controller) {
        this.controller = controller;
        this.eventListView = new ListView<>();    // Inizializza la ListView vuota
    }

    /**
     * Crea la tab "Eventi", non consentendone la chiusura,
     * e ne imposta il contenuto con createContent().
     * @return Tab configurata per gli Eventi
     */
    public Tab createTab() {
        Tab tab = new Tab("Eventi");
        tab.setClosable(false);                    // L'utente non può chiudere la scheda
        tab.setContent(createContent());
        return tab;
    }

    /**
     * Costruisce il layout principale della scheda:
     * - titolo
     * - ListView per gli eventi
     * - HBox con i pulsanti di azione
     * @return VBox contenente l'interfaccia degli Eventi
     */
    private VBox createContent() {
        VBox content = new VBox(10);               // Spaziatura verticale di 10px
        content.setPadding(new Insets(10));        // Margine interno di 10px

        // Titolo della sezione
        Label title = new Label("Gestione Eventi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ListView per elencare gli eventi; altezza prefissata
        eventListView.setPrefHeight(300);

        // Contenitore orizzontale per i bottoni di azione
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);          // Allinea i bottoni al centro

        // Pulsanti per le operazioni CRUD e dettaglio
        Button addEventBtn    = new Button("Aggiungi Evento");
        Button editEventBtn   = new Button("Modifica Evento");
        Button deleteEventBtn = new Button("Elimina Evento");
        Button viewEventBtn   = new Button("Visualizza Dettagli");

        // Aggiunge i pulsanti all'HBox
        buttons.getChildren().addAll(
            addEventBtn, editEventBtn, deleteEventBtn, viewEventBtn
        );

        // Associa le azioni ai pulsanti
        addEventBtn.setOnAction(e -> showAddEventDialog());
        editEventBtn.setOnAction(e -> showEditEventDialog());
        deleteEventBtn.setOnAction(e -> deleteSelectedEvent());
        viewEventBtn.setOnAction(e -> showEventDetails());

        // Aggiunge titolo, lista e bottoni al VBox principale
        content.getChildren().addAll(title, eventListView, buttons);
        return content;
    }

    /**
     * Mostra il dialog per aggiungere un nuovo evento.
     * Se l'utente conferma, ricarica i dati e mostra un alert di successo.
     */
    private void showAddEventDialog() {
        EventDialog dialog = new EventDialog(controller, null);
        dialog.showAndWait().ifPresent(event -> {
            refreshData();
            AlertUtils.showSuccess("Evento creato con successo!");
        });
    }

    /**
     * Mostra il dialog per modificare l'evento selezionato.
     * Se nessuno è selezionato, mostra un warning.
     * Al termine, ricarica i dati e mostra alert di successo.
     */
    private void showEditEventDialog() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un evento da modificare.");
            return;
        }
        EventDialog dialog = new EventDialog(controller, selected);
        dialog.showAndWait().ifPresent(event -> {
            refreshData();
            AlertUtils.showSuccess("Evento modificato con successo!");
        });
    }

    /**
     * Elimina l'evento selezionato dopo conferma.
     * Mostra warning se nessuno è selezionato.
     * Al termine, ricarica la lista e mostra alert di successo.
     */
    private void deleteSelectedEvent() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un evento da eliminare.");
            return;
        }
        if (AlertUtils.confirmDelete("evento", selected.getName())) {
            controller.deleteEvent(selected.getId());
            refreshData();
            AlertUtils.showSuccess("Evento eliminato con successo!");
        }
    }

    /**
     * Mostra i dettagli completi dell'evento selezionato in un Alert.
     * Includendo data, località, numero ospiti e lista dei menu collegati.
     * Se nessuno è selezionato, mostra un warning.
     */
    private void showEventDetails() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un evento da visualizzare.");
            return;
        }

        // Costruisce il testo con le informazioni dell'evento
        StringBuilder content = new StringBuilder();
        content.append("Data: ").append(selected.getDate()).append("\n");
        content.append("Località: ").append(selected.getLocation()).append("\n");
        content.append("Numero ospiti: ").append(selected.getNumberOfGuests()).append("\n");
        content.append("Menu associati: ").append(selected.getMenus().size()).append("\n\n");

        // Se ci sono menu, elencali uno per riga con nome e prezzo
        if (!selected.getMenus().isEmpty()) {
            content.append("Lista Menu:\n");
            for (com.catering.model.Menu menu : selected.getMenus()) {
                content.append("• ").append(menu.getName())
                       .append(" - €").append(menu.getPrice()).append("\n");
            }
        }

        AlertUtils.showInfo(
            "Dettagli Evento",
            "Dettagli di: " + selected.getName(),
            content.toString()
        );
    }

    /**
     * Ricarica tutti gli eventi dal controller
     * e popola la ListView. Da chiamare dopo ogni modifica.
     */
    public void refreshData() {
        eventListView.getItems().setAll(controller.getAllEvents());
    }
}
