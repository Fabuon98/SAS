// ChefViewFX.java - View principale dell'applicazione
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Optional;

public class ChefViewFX extends Application {
    private ChefController controller;
    private TabPane tabPane;
    
    // Liste per visualizzare i dati
    private ListView<Event> eventListView;
    private ListView<com.catering.model.Menu> menuListView;
    private ListView<Section> sectionListView;
    private ListView<Recipe> recipeListView;
    
    @Override
    public void start(Stage primaryStage) {
        controller = new ChefController();
        
        primaryStage.setTitle("Gestione Chef - Applicazione per Eventi e Menu");
        
        // Crea il TabPane principale
        tabPane = new TabPane();
        
        // Crea le tab per ogni sezione
        Tab eventsTab = createEventsTab();
        Tab menusTab = createMenusTab();
        Tab sectionsTab = createSectionsTab();
        Tab recipesTab = createRecipesTab();
        
        tabPane.getTabs().addAll(eventsTab, menusTab, sectionsTab, recipesTab);
        
        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Carica i dati iniziali
        refreshAllLists();
    }
    
    // ========== TAB EVENTI ==========
    private Tab createEventsTab() {
        Tab tab = new Tab("Eventi");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Titolo
        Label title = new Label("Gestione Eventi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Lista eventi
        eventListView = new ListView<>();
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
        tab.setContent(content);
        return tab;
    }
    
    // ========== TAB MENU ==========
    private Tab createMenusTab() {
        Tab tab = new Tab("Menu");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        menuListView = new ListView<>();
        menuListView.setPrefHeight(300);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addMenuBtn = new Button("Aggiungi Menu");
        Button editMenuBtn = new Button("Modifica Menu");
        Button deleteMenuBtn = new Button("Elimina Menu");
        Button viewMenuBtn = new Button("Visualizza Menu");
        
        buttons.getChildren().addAll(addMenuBtn, editMenuBtn, deleteMenuBtn, viewMenuBtn);
        
        addMenuBtn.setOnAction(e -> showAddMenuDialog());
        editMenuBtn.setOnAction(e -> showEditMenuDialog());
        deleteMenuBtn.setOnAction(e -> deleteSelectedMenu());
        viewMenuBtn.setOnAction(e -> showMenuDetails());
        
        content.getChildren().addAll(title, menuListView, buttons);
        tab.setContent(content);
        return tab;
    }
    
    // ========== TAB SEZIONI ==========
    private Tab createSectionsTab() {
        Tab tab = new Tab("Sezioni");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Sezioni Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        sectionListView = new ListView<>();
        sectionListView.setPrefHeight(300);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addSectionBtn = new Button("Aggiungi Sezione");
        Button editSectionBtn = new Button("Modifica Sezione");
        Button deleteSectionBtn = new Button("Elimina Sezione");
        
        buttons.getChildren().addAll(addSectionBtn, editSectionBtn, deleteSectionBtn);
        
        addSectionBtn.setOnAction(e -> showAddSectionDialog());
        editSectionBtn.setOnAction(e -> showEditSectionDialog());
        deleteSectionBtn.setOnAction(e -> deleteSelectedSection());
        
        content.getChildren().addAll(title, sectionListView, buttons);
        tab.setContent(content);
        return tab;
    }
    
    // ========== TAB RICETTE ==========
    private Tab createRecipesTab() {
        Tab tab = new Tab("Ricette");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        Label title = new Label("Gestione Ricette");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        recipeListView = new ListView<>();
        recipeListView.setPrefHeight(300);
        
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        
        Button addRecipeBtn = new Button("Aggiungi Ricetta");
        Button editRecipeBtn = new Button("Modifica Ricetta");
        Button deleteRecipeBtn = new Button("Elimina Ricetta");
        Button viewRecipeBtn = new Button("Visualizza Ricetta");
        Button editIngredientsBtn = new Button("Gestisci Ingredienti");
        Button editPreparationsBtn = new Button("Gestisci Preparazioni");
        
        // Prima riga di bottoni
        HBox firstRowButtons = new HBox(10);
        firstRowButtons.setAlignment(Pos.CENTER);
        firstRowButtons.getChildren().addAll(addRecipeBtn, editRecipeBtn, deleteRecipeBtn);
        
        // Seconda riga di bottoni
        HBox secondRowButtons = new HBox(10);
        secondRowButtons.setAlignment(Pos.CENTER);
        secondRowButtons.getChildren().addAll(viewRecipeBtn, editIngredientsBtn, editPreparationsBtn);
        
        addRecipeBtn.setOnAction(e -> showAddRecipeDialog());
        editRecipeBtn.setOnAction(e -> showEditRecipeDialog());
        deleteRecipeBtn.setOnAction(e -> deleteSelectedRecipe());
        viewRecipeBtn.setOnAction(e -> showRecipeDetails());
        editIngredientsBtn.setOnAction(e -> showIngredientsDialog());
        editPreparationsBtn.setOnAction(e -> showPreparationsDialog());
        
        content.getChildren().addAll(title, recipeListView, firstRowButtons, secondRowButtons);
        tab.setContent(content);
        return tab;
    }
    
    // ========== DIALOG EVENTI ==========
    private void showAddEventDialog() {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuovo Evento");
        dialog.setHeaderText("Inserisci i dettagli del nuovo evento");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField locationField = new TextField();
        TextField guestsField = new TextField();
        
        grid.add(new Label("Nome Evento:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Località:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Numero Ospiti:"), 0, 3);
        grid.add(guestsField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    LocalDate date = datePicker.getValue();
                    String location = locationField.getText().trim();
                    int guests = Integer.parseInt(guestsField.getText().trim());
                    
                    if (!name.isEmpty() && !location.isEmpty() && guests > 0) {
                        return controller.createEvent(name, date, location, guests);
                    }
                } catch (NumberFormatException e) {
                    showAlert("Errore", "Il numero di ospiti deve essere un numero valido.");
                }
            }
            return null;
        });
        
        Optional<Event> result = dialog.showAndWait();
        if (result.isPresent()) {
            refreshAllLists();
            showAlert("Successo", "Evento creato con successo!");
        }
    }
    
    private void showEditEventDialog() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un evento da modificare.");
            return;
        }
        
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modifica Evento");
        dialog.setHeaderText("Modifica i dettagli dell'evento");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        DatePicker datePicker = new DatePicker(selected.getDate());
        TextField locationField = new TextField(selected.getLocation());
        TextField guestsField = new TextField(String.valueOf(selected.getNumberOfGuests()));
        
        grid.add(new Label("Nome Evento:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Data:"), 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(new Label("Località:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Numero Ospiti:"), 0, 3);
        grid.add(guestsField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    selected.setName(nameField.getText().trim());
                    selected.setDate(datePicker.getValue());
                    selected.setLocation(locationField.getText().trim());
                    selected.setNumberOfGuests(Integer.parseInt(guestsField.getText().trim()));
                    return controller.updateEvent(selected);
                } catch (NumberFormatException e) {
                    showAlert("Errore", "Il numero di ospiti deve essere un numero valido.");
                }
            }
            return false;
        });
        
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshAllLists();
            showAlert("Successo", "Evento modificato con successo!");
        }
    }
    
    private void deleteSelectedEvent() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un evento da eliminare.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Conferma Eliminazione");
        confirmation.setHeaderText("Sei sicuro di voler eliminare questo evento?");
        confirmation.setContentText("Evento: " + selected.getName());
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.deleteEvent(selected.getId());
            refreshAllLists();
            showAlert("Successo", "Evento eliminato con successo!");
        }
    }
    
    private void showEventDetails() {
        Event selected = eventListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un evento da visualizzare.");
            return;
        }
        
        Alert details = new Alert(Alert.AlertType.INFORMATION);
        details.setTitle("Dettagli Evento");
        details.setHeaderText("Dettagli di: " + selected.getName());
        
        String content = "Data: " + selected.getDate() + "\n" +
                        "Località: " + selected.getLocation() + "\n" +
                        "Numero ospiti: " + selected.getNumberOfGuests() + "\n" +
                        "Menu associati: " + selected.getMenus().size();
        
        details.setContentText(content);
        details.showAndWait();
    }
    
    // ========== DIALOG MENU ==========
    private void showAddMenuDialog() {
        Dialog<com.catering.model.Menu> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuovo Menu");
        dialog.setHeaderText("Inserisci i dettagli del nuovo menu");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        TextField priceField = new TextField();
        
        grid.add(new Label("Nome Menu:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Prezzo (€):"), 0, 2);
        grid.add(priceField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    String description = descriptionArea.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    
                    if (!name.isEmpty() && price >= 0) {
                        return controller.createMenu(name, description, price);
                    }
                } catch (NumberFormatException e) {
                    showAlert("Errore", "Il prezzo deve essere un numero valido.");
                }
            }
            return null;
        });
        
        Optional<com.catering.model.Menu> result = dialog.showAndWait();
        if (result.isPresent()) {
            refreshAllLists();
            showAlert("Successo", "Menu creato con successo!");
        }
    }
    
    private void showEditMenuDialog() {
        com.catering.model.Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un menu da modificare.");
            return;
        }
        
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modifica Menu");
        dialog.setHeaderText("Modifica i dettagli del menu");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        TextArea descriptionArea = new TextArea(selected.getDescription());
        descriptionArea.setPrefRowCount(3);
        TextField priceField = new TextField(String.valueOf(selected.getPrice()));
        
        grid.add(new Label("Nome Menu:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Prezzo (€):"), 0, 2);
        grid.add(priceField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    selected.setName(nameField.getText().trim());
                    selected.setDescription(descriptionArea.getText().trim());
                    selected.setPrice(Double.parseDouble(priceField.getText().trim()));
                    return controller.updateMenu(selected);
                } catch (NumberFormatException e) {
                    showAlert("Errore", "Il prezzo deve essere un numero valido.");
                }
            }
            return false;
        });
        
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshAllLists();
            showAlert("Successo", "Menu modificato con successo!");
        }
    }
    
    private void deleteSelectedMenu() {
        com.catering.model.Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un menu da eliminare.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Conferma Eliminazione");
        confirmation.setHeaderText("Sei sicuro di voler eliminare questo menu?");
        confirmation.setContentText("Menu: " + selected.getName());
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.deleteMenu(selected.getId());
            refreshAllLists();
            showAlert("Successo", "Menu eliminato con successo!");
        }
    }
    
    private void showMenuDetails() {
        com.catering.model.Menu selected = menuListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona un menu da visualizzare.");
            return;
        }
        
        Alert details = new Alert(Alert.AlertType.INFORMATION);
        details.setTitle("Dettagli Menu");
        details.setHeaderText("Dettagli di: " + selected.getName());
        
        String content = "Descrizione: " + selected.getDescription() + "\n" +
                        "Prezzo: €" + selected.getPrice() + "\n" +
                        "Sezioni: " + selected.getSections().size();
        
        details.setContentText(content);
        details.showAndWait();
    }
    
    // ========== DIALOG SEZIONI ==========
    private void showAddSectionDialog() {
        Dialog<Section> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuova Sezione");
        dialog.setHeaderText("Inserisci i dettagli della nuova sezione");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        
        grid.add(new Label("Nome Sezione:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                if (!name.isEmpty()) {
                    return controller.createSection(name, description);
                }
            }
            return null;
        });
        
        Optional<Section> result = dialog.showAndWait();
        if (result.isPresent()) {
            refreshAllLists();
            showAlert("Successo", "Sezione creata con successo!");
        }
    }
    
    private void showEditSectionDialog() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una sezione da modificare.");
            return;
        }
        
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modifica Sezione");
        dialog.setHeaderText("Modifica i dettagli della sezione");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        TextArea descriptionArea = new TextArea(selected.getDescription());
        descriptionArea.setPrefRowCount(3);
        
        grid.add(new Label("Nome Sezione:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                selected.setName(nameField.getText().trim());
                selected.setDescription(descriptionArea.getText().trim());
                return controller.updateSection(selected);
            }
            return false;
        });
        
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshAllLists();
            showAlert("Successo", "Sezione modificata con successo!");
        }
    }
    
    private void deleteSelectedSection() {
        Section selected = sectionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una sezione da eliminare.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Conferma Eliminazione");
        confirmation.setHeaderText("Sei sicuro di voler eliminare questa sezione?");
        confirmation.setContentText("Sezione: " + selected.getName());
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.deleteSection(selected.getId());
            refreshAllLists();
            showAlert("Successo", "Sezione eliminata con successo!");
        }
    }
    
    // ========== DIALOG RICETTE ==========
    private void showAddRecipeDialog() {
        Dialog<Recipe> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuova Ricetta");
        dialog.setHeaderText("Inserisci i dettagli della nuova ricetta");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(2);
        TextField prepTimeField = new TextField();
        TextField cookTimeField = new TextField();
        TextField servingsField = new TextField();
        ComboBox<String> difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("Facile", "Medio", "Difficile");
        difficultyCombo.setValue("Facile");
        
        grid.add(new Label("Nome Ricetta:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Tempo prep. (min):"), 0, 2);
        grid.add(prepTimeField, 1, 2);
        grid.add(new Label("Tempo cottura (min):"), 0, 3);
        grid.add(cookTimeField, 1, 3);
        grid.add(new Label("Porzioni:"), 0, 4);
        grid.add(servingsField, 1, 4);
        grid.add(new Label("Difficoltà:"), 0, 5);
        grid.add(difficultyCombo, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    String description = descriptionArea.getText().trim();
                    
                    if (!name.isEmpty()) {
                        Recipe recipe = controller.createRecipe(name, description);
                        
                        if (!prepTimeField.getText().trim().isEmpty()) {
                            recipe.setPreparationTime(Integer.parseInt(prepTimeField.getText().trim()));
                        }
                        if (!cookTimeField.getText().trim().isEmpty()) {
                            recipe.setCookingTime(Integer.parseInt(cookTimeField.getText().trim()));
                        }
                        if (!servingsField.getText().trim().isEmpty()) {
                            recipe.setServings(Integer.parseInt(servingsField.getText().trim()));
                        }
                        recipe.setDifficulty(difficultyCombo.getValue());
                        
                        return recipe;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Errore", "I campi numerici devono contenere numeri validi.");
                }
            }
            return null;
        });
        
        Optional<Recipe> result = dialog.showAndWait();
        if (result.isPresent()) {
            refreshAllLists();
            showAlert("Successo", "Ricetta creata con successo!");
        }
    }
    
    private void showEditRecipeDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una ricetta da modificare.");
            return;
        }
        
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Modifica Ricetta");
        dialog.setHeaderText("Modifica i dettagli della ricetta");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField(selected.getName());
        TextArea descriptionArea = new TextArea(selected.getDescription());
        descriptionArea.setPrefRowCount(2);
        TextField prepTimeField = new TextField(String.valueOf(selected.getPreparationTime()));
        TextField cookTimeField = new TextField(String.valueOf(selected.getCookingTime()));
        TextField servingsField = new TextField(String.valueOf(selected.getServings()));
        ComboBox<String> difficultyCombo = new ComboBox<>();
        difficultyCombo.getItems().addAll("Facile", "Medio", "Difficile");
        difficultyCombo.setValue(selected.getDifficulty() != null ? selected.getDifficulty() : "Facile");
        
        grid.add(new Label("Nome Ricetta:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Descrizione:"), 0, 1);
        grid.add(descriptionArea, 1, 1);
        grid.add(new Label("Tempo prep. (min):"), 0, 2);
        grid.add(prepTimeField, 1, 2);
        grid.add(new Label("Tempo cottura (min):"), 0, 3);
        grid.add(cookTimeField, 1, 3);
        grid.add(new Label("Porzioni:"), 0, 4);
        grid.add(servingsField, 1, 4);
        grid.add(new Label("Difficoltà:"), 0, 5);
        grid.add(difficultyCombo, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    selected.setName(nameField.getText().trim());
                    selected.setDescription(descriptionArea.getText().trim());
                    selected.setPreparationTime(Integer.parseInt(prepTimeField.getText().trim()));
                    selected.setCookingTime(Integer.parseInt(cookTimeField.getText().trim()));
                    selected.setServings(Integer.parseInt(servingsField.getText().trim()));
                    selected.setDifficulty(difficultyCombo.getValue());
                    return controller.updateRecipe(selected);
                } catch (NumberFormatException e) {
                    showAlert("Errore", "I campi numerici devono contenere numeri validi.");
                }
            }
            return false;
        });
        
        Optional<Boolean> result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            refreshAllLists();
            showAlert("Successo", "Ricetta modificata con successo!");
        }
    }
    
    private void deleteSelectedRecipe() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una ricetta da eliminare.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Conferma Eliminazione");
        confirmation.setHeaderText("Sei sicuro di voler eliminare questa ricetta?");
        confirmation.setContentText("Ricetta: " + selected.getName());
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.deleteRecipe(selected.getId());
            refreshAllLists();
            showAlert("Successo", "Ricetta eliminata con successo!");
        }
    }
    
    private void showRecipeDetails() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una ricetta da visualizzare.");
            return;
        }
        
        Alert details = new Alert(Alert.AlertType.INFORMATION);
        details.setTitle("Dettagli Ricetta");
        details.setHeaderText("Ricetta: " + selected.getName());
        
        StringBuilder content = new StringBuilder();
        content.append("Descrizione: ").append(selected.getDescription()).append("\n");
        content.append("Tempo preparazione: ").append(selected.getPreparationTime()).append(" min\n");
        content.append("Tempo cottura: ").append(selected.getCookingTime()).append(" min\n");
        content.append("Porzioni: ").append(selected.getServings()).append("\n");
        content.append("Difficoltà: ").append(selected.getDifficulty()).append("\n");
        content.append("Ingredienti: ").append(selected.getIngredients().size()).append("\n");
        content.append("Preparazioni: ").append(selected.getPreparations().size());
        
        details.setContentText(content.toString());
        details.showAndWait();
    }
    
    // ========== GESTIONE INGREDIENTI ==========
    private void showIngredientsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una ricetta per gestire gli ingredienti.");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Gestione Ingredienti");
        dialog.setHeaderText("Ingredienti per: " + selected.getName());
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        ListView<String> ingredientsList = new ListView<>();
        ingredientsList.getItems().addAll(selected.getIngredients());
        ingredientsList.setPrefHeight(200);
        
        HBox inputBox = new HBox(10);
        TextField newIngredientField = new TextField();
        newIngredientField.setPromptText("Nuovo ingrediente...");
        Button addIngredientBtn = new Button("Aggiungi");
        Button removeIngredientBtn = new Button("Rimuovi Selezionato");
        
        inputBox.getChildren().addAll(newIngredientField, addIngredientBtn, removeIngredientBtn);
        
        // Eventi per i bottoni degli ingredienti
        addIngredientBtn.setOnAction(e -> {
            String newIngredient = newIngredientField.getText().trim();
            if (!newIngredient.isEmpty()) {
                selected.addIngredient(newIngredient);
                ingredientsList.getItems().add(newIngredient);
                newIngredientField.clear();
            }
        });
        
        removeIngredientBtn.setOnAction(e -> {
            String selectedIngredient = ingredientsList.getSelectionModel().getSelectedItem();
            if (selectedIngredient != null) {
                selected.getIngredients().remove(selectedIngredient);
                ingredientsList.getItems().remove(selectedIngredient);
            }
        });
        
        // Permetti di aggiungere ingredienti premendo Enter
        newIngredientField.setOnAction(e -> addIngredientBtn.fire());
        
        content.getChildren().addAll(
            new Label("Lista Ingredienti:"),
            ingredientsList,
            inputBox
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
    
    // ========== GESTIONE PREPARAZIONI ==========
    private void showPreparationsDialog() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attenzione", "Seleziona una ricetta per gestire le preparazioni.");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Gestione Preparazioni");
        dialog.setHeaderText("Preparazioni per: " + selected.getName());
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        ListView<Preparation> preparationsList = new ListView<>();
        preparationsList.getItems().addAll(selected.getPreparations());
        preparationsList.setPrefHeight(200);
        
        // Form per nuova preparazione
        GridPane prepForm = new GridPane();
        prepForm.setHgap(10);
        prepForm.setVgap(10);
        
        TextField stepField = new TextField();
        stepField.setPromptText("Numero step");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descrizione del passaggio...");
        descriptionArea.setPrefRowCount(2);
        TextField timeField = new TextField();
        timeField.setPromptText("Tempo in minuti");
        
        prepForm.add(new Label("Step:"), 0, 0);
        prepForm.add(stepField, 1, 0);
        prepForm.add(new Label("Descrizione:"), 0, 1);
        prepForm.add(descriptionArea, 1, 1);
        prepForm.add(new Label("Tempo (min):"), 0, 2);
        prepForm.add(timeField, 1, 2);
        
        HBox buttonBox = new HBox(10);
        Button addPrepBtn = new Button("Aggiungi Preparazione");
        Button removePrepBtn = new Button("Rimuovi Selezionata");
        Button editPrepBtn = new Button("Modifica Selezionata");
        
        buttonBox.getChildren().addAll(addPrepBtn, removePrepBtn, editPrepBtn);
        
        // Eventi per i bottoni delle preparazioni
        addPrepBtn.setOnAction(e -> {
            try {
                int step = Integer.parseInt(stepField.getText().trim());
                String description = descriptionArea.getText().trim();
                int time = Integer.parseInt(timeField.getText().trim());
                
                if (!description.isEmpty()) {
                    Preparation prep = controller.createPreparation(step, description, time);
                    selected.addPreparation(prep);
                    preparationsList.getItems().add(prep);
                    
                    // Pulisci i campi
                    stepField.clear();
                    descriptionArea.clear();
                    timeField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Errore", "Step e tempo devono essere numeri validi.");
            }
        });
        
        removePrepBtn.setOnAction(e -> {
            Preparation selectedPrep = preparationsList.getSelectionModel().getSelectedItem();
            if (selectedPrep != null) {
                selected.getPreparations().remove(selectedPrep);
                preparationsList.getItems().remove(selectedPrep);
            }
        });
        
        editPrepBtn.setOnAction(e -> {
            Preparation selectedPrep = preparationsList.getSelectionModel().getSelectedItem();
            if (selectedPrep != null) {
                stepField.setText(String.valueOf(selectedPrep.getStepNumber()));
                descriptionArea.setText(selectedPrep.getDescription());
                timeField.setText(String.valueOf(selectedPrep.getTimeInMinutes()));
                
                // Rimuovi la preparazione esistente così può essere ri-aggiunta modificata
                selected.getPreparations().remove(selectedPrep);
                preparationsList.getItems().remove(selectedPrep);
            }
        });
        
        content.getChildren().addAll(
            new Label("Lista Preparazioni:"),
            preparationsList,
            new Separator(),
            new Label("Aggiungi/Modifica Preparazione:"),
            prepForm,
            buttonBox
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
    
    // ========== METODI UTILITY ==========
    private void refreshAllLists() {
        eventListView.getItems().clear();
        eventListView.getItems().addAll(controller.getAllEvents());
        
        menuListView.getItems().clear();
        menuListView.getItems().addAll(controller.getAllMenus());
        
        sectionListView.getItems().clear();
        sectionListView.getItems().addAll(controller.getAllSections());
        
        recipeListView.getItems().clear();
        recipeListView.getItems().addAll(controller.getAllRecipes());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        Application.launch(ChefViewFX.class, args);
    }
}