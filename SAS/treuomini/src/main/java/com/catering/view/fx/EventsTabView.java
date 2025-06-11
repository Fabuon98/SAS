// EventsTabView.java - Gestione della tab Eventi
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EventsTabView {
    private ChefController controller;
    private ListView<Event> eventListView;
    
    public EventsTabView(ChefController controller) {
        this.controller = controller;
        this.eventListView = new ListView<>();
    }
    
    public Tab createTab() {
        Tab tab = new Tab("Eventi");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }
    
    private VBox createContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Titolo
        Label title = new Label("Gestione Eventi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Lista eventi
        eventListView.setPrefHeight(300);
        
        // Bottoni
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addEventBtn = new Button("Aggiungi Evento");
        Button editEventBtn = new Button("Modifica Evento");
        Button deleteEventBtn = new Button("Elimina Evento");
        Button viewEventBtn = new Button("Visualizza Dettagli");
        
        buttons.getChildren().addAll(addEventBtn, editEventBtn, deleteEventBtn, viewEventBtn);
        
        // Eventi bottoni
        addEventBtn.setOnAction(e -> showAddEventDialog());
        editEventBtn.setOnAction(e -> showEditEventDialog());
        deleteEventBtn.setOnAction(e -> deleteSelectedEvent());
        viewEventBtn.setOnAction(e -> showEventDetails());
        
        content.getChildren().addAll(title, eventListView, buttons);
        return content;
    }
    
    private void showAddEventDialog() {
        EventDialog dialog = new EventDialog(controller, null);
        dialog.showAndWait().ifPresent(event -> {
            refreshData();
            AlertUtils.showSuccess("Evento creato con successo!");
        });
    }
    
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
    
    private void showEventDetails() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleziona un evento da visualizzare.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        content.append("Data: ").append(selected.getDate()).append("\n");
        content.append("Località: ").append(selected.getLocation()).append("\n");
        content.append("Numero ospiti: ").append(selected.getNumberOfGuests()).append("\n");
        content.append("Menu associati: ").append(selected.getMenus().size()).append("\n\n");
        
        if (!selected.getMenus().isEmpty()) {
            content.append("Lista Menu:\n");
            for (com.catering.model.Menu menu : selected.getMenus()) {
                content.append("• ").append(menu.getName())
                       .append(" - €").append(menu.getPrice()).append("\n");
            }
        }
        
        AlertUtils.showInfo("Dettagli Evento", "Dettagli di: " + selected.getName(), content.toString());
    }
    
    public void refreshData() {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(controller.getAllEvents());
    }
}