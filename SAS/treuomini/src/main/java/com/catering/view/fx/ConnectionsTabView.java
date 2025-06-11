// ConnectionsTabView.java - Gestione della tab Collegamenti
package com.catering.view.fx;

import com.catering.controller.ChefController;
import com.catering.model.Event;
import com.catering.model.Recipe;
import com.catering.model.Section;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConnectionsTabView {
    private ChefController controller;
    
    // ComboBox per i collegamenti
    private ComboBox<Event> eventCombo;
    private ComboBox<com.catering.model.Menu> menuCombo1, menuCombo2;
    private ComboBox<Section> sectionCombo1, sectionCombo2;
    private ComboBox<Recipe> recipeCombo;
    
    public ConnectionsTabView(ChefController controller) {
        this.controller = controller;
        initializeComboBoxes();
    }
    
    public Tab createTab() {
        Tab tab = new Tab("Collegamenti");
        tab.setClosable(false);
        tab.setContent(createContent());
        return tab;
    }
    
    private void initializeComboBoxes() {
        eventCombo = new ComboBox<>();
        menuCombo1 = new ComboBox<>();
        menuCombo2 = new ComboBox<>();
        sectionCombo1 = new ComboBox<>();
        sectionCombo2 = new ComboBox<>();
        recipeCombo = new ComboBox<>();
    }
    
    private ScrollPane createContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(15));
        
        Label title = new Label("Gestione Collegamenti");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        content.getChildren().addAll(
            title,
            new Separator(),
            createEventMenuConnectionSection(),
            new Separator(),
            createMenuSectionConnectionSection(),
            new Separator(),
            createSectionRecipeConnectionSection()
        );
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    private VBox createEventMenuConnectionSection() {
        VBox section = new VBox(10);
        
        Label sectionTitle = new Label("Collega Menu agli Eventi");
        sectionTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Seleziona Evento:"), 0, 0);
        grid.add(eventCombo, 1, 0);
        grid.add(new Label("Seleziona Menu:"), 0, 1);
        grid.add(menuCombo1, 1, 1);
        
        HBox buttons = new HBox(10);
        
        Button connectButton = new Button("Collega Menu all'Evento");
        Button viewEventMenusButton = new Button("Visualizza Menu dell'Evento");
        
        buttons.getChildren().addAll(connectButton, viewEventMenusButton);
        
        connectButton.setOnAction(e -> connectMenuToEvent());
        viewEventMenusButton.setOnAction(e -> showEventMenus());
        
        section.getChildren().addAll(sectionTitle, grid, buttons);
        return section;
    }
    
    private VBox createMenuSectionConnectionSection() {
        VBox section = new VBox(10);
        
        Label sectionTitle = new Label("Collega Sezioni ai Menu");
        sectionTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Seleziona Menu:"), 0, 0);
        grid.add(menuCombo2, 1, 0);
        grid.add(new Label("Seleziona Sezione:"), 0, 1);
        grid.add(sectionCombo1, 1, 1);
        
        HBox buttons = new HBox(10);
        
        Button connectButton = new Button("Collega Sezione al Menu");
        Button viewMenuSectionsButton = new Button("Visualizza Sezioni del Menu");
        
        buttons.getChildren().addAll(connectButton, viewMenuSectionsButton);
        
        connectButton.setOnAction(e -> connectSectionToMenu());
        viewMenuSectionsButton.setOnAction(e -> showMenuSections());
        
        section.getChildren().addAll(sectionTitle, grid, buttons);
        return section;
    }
    
    private VBox createSectionRecipeConnectionSection() {
        VBox section = new VBox(10);
        
        Label sectionTitle = new Label("Collega Ricette alle Sezioni");
        sectionTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("Seleziona Sezione:"), 0, 0);
        grid.add(sectionCombo2, 1, 0);
        grid.add(new Label("Seleziona Ricetta:"), 0, 1);
        grid.add(recipeCombo, 1, 1);
        
        HBox buttons = new HBox(10);
        
        Button connectButton = new Button("Collega Ricetta alla Sezione");
        Button connectWithNewNameButton = new Button("Collega con Nuovo Nome");
        Button viewSectionRecipesButton = new Button("Visualizza Ricette della Sezione");
        
        buttons.getChildren().addAll(connectButton, connectWithNewNameButton, viewSectionRecipesButton);
        
        connectButton.setOnAction(e -> connectRecipeToSection(false));
        connectWithNewNameButton.setOnAction(e -> connectRecipeToSection(true));
        viewSectionRecipesButton.setOnAction(e -> showSectionRecipes());
        
        section.getChildren().addAll(sectionTitle, grid, buttons);
        return section;
    }
    
    // ========== METODI DI COLLEGAMENTO ==========
    
    private void connectMenuToEvent() {
        Event selectedEvent = eventCombo.getValue();
        com.catering.model.Menu selectedMenu = menuCombo1.getValue();
        
        if (selectedEvent != null && selectedMenu != null) {
            try {
                controller.addMenuToEvent(selectedEvent.getId(), selectedMenu.getId());
                AlertUtils.showSuccess("Menu collegato all'evento con successo!");
                refreshComboBoxes();
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il collegamento: " + e.getMessage());
            }
        } else {
            AlertUtils.showWarning("Seleziona sia un evento che un menu.");
        }
    }
    
    private void connectSectionToMenu() {
        com.catering.model.Menu selectedMenu = menuCombo2.getValue();
        Section selectedSection = sectionCombo1.getValue();
        
        if (selectedMenu != null && selectedSection != null) {
            try {
                controller.addSectionToMenu(selectedMenu.getId(), selectedSection.getId());
                AlertUtils.showSuccess("Sezione collegata al menu con successo!");
                refreshComboBoxes();
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il collegamento: " + e.getMessage());
            }
        } else {
            AlertUtils.showWarning("Seleziona sia un menu che una sezione.");
        }
    }
    
    private void connectRecipeToSection(boolean withNewName) {
        Section selectedSection = sectionCombo2.getValue();
        Recipe selectedRecipe = recipeCombo.getValue();
        
        if (selectedSection == null || selectedRecipe == null) {
            AlertUtils.showWarning("Seleziona sia una sezione che una ricetta.");
            return;
        }
        
        if (withNewName) {
            showRecipeWithNewNameDialog(selectedSection, selectedRecipe);
        } else {
            try {
                controller.addRecipeToSection(selectedSection.getId(), selectedRecipe.getId());
                AlertUtils.showSuccess("Ricetta collegata alla sezione con successo!");
                refreshComboBoxes();
            } catch (Exception e) {
                AlertUtils.showError("Errore durante il collegamento: " + e.getMessage());
            }
        }
    }
    
    // ========== METODI DI VISUALIZZAZIONE ==========
    
    private void showEventMenus() {
        Event selectedEvent = eventCombo.getValue();
        if (selectedEvent == null) {
            AlertUtils.showWarning("Seleziona un evento.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        if (selectedEvent.getMenus().isEmpty()) {
            content.append("Nessun menu collegato a questo evento.");
        } else {
            content.append("Menu collegati:\n\n");
            for (com.catering.model.Menu menu : selectedEvent.getMenus()) {
                content.append("• ").append(menu.getName())
                       .append(" - €").append(menu.getPrice()).append("\n");
            }
        }
        
        AlertUtils.showInfo("Menu dell'Evento", 
            "Menu collegati all'evento: " + selectedEvent.getName(), 
            content.toString());
    }
    
    private void showMenuSections() {
        com.catering.model.Menu selectedMenu = menuCombo2.getValue();
        if (selectedMenu == null) {
            AlertUtils.showWarning("Seleziona un menu.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        if (selectedMenu.getSections().isEmpty()) {
            content.append("Nessuna sezione collegata a questo menu.");
        } else {
            content.append("Sezioni collegate:\n\n");
            for (Section section : selectedMenu.getSections()) {
                content.append("• ").append(section.getName()).append("\n");
                content.append("  ").append(section.getDescription()).append("\n\n");
            }
        }
        
        AlertUtils.showInfo("Sezioni del Menu", 
            "Sezioni del menu: " + selectedMenu.getName(), 
            content.toString());
    }
    
    private void showSectionRecipes() {
        Section selectedSection = sectionCombo2.getValue();
        if (selectedSection == null) {
            AlertUtils.showWarning("Seleziona una sezione.");
            return;
        }
        
        StringBuilder content = new StringBuilder();
        if (selectedSection.getRecipes().isEmpty()) {
            content.append("Nessuna ricetta collegata a questa sezione.");
        } else {
            content.append("Ricette collegate:\n\n");
            for (Recipe recipe : selectedSection.getRecipes()) {
                content.append("• ").append(recipe.getName()).append("\n");
                if (recipe.getDifficulty() != null) {
                    content.append("  Difficoltà: ").append(recipe.getDifficulty()).append("\n");
                }
                if (recipe.getPreparationTime() > 0) {
                    content.append("  Tempo preparazione: ").append(recipe.getPreparationTime()).append(" min\n");
                }
                content.append("\n");
            }
        }
        
        AlertUtils.showInfo("Ricette della Sezione", 
            "Ricette della sezione: " + selectedSection.getName(), 
            content.toString());
    }
    
    // ========== DIALOG PER RICETTA CON NUOVO NOME ==========
    
    private void showRecipeWithNewNameDialog(Section section, Recipe originalRecipe) {
        RecipeWithNewNameDialog dialog = new RecipeWithNewNameDialog(controller, section, originalRecipe);
        dialog.showAndWait().ifPresent(success -> {
            if (success) {
                refreshComboBoxes();
            }
        });
    }
    
    // ========== METODI UTILITY ==========
    
    public void refreshComboBoxes() {
        if (eventCombo != null) {
            eventCombo.getItems().clear();
            eventCombo.getItems().addAll(controller.getAllEvents());
        }
        
        if (menuCombo1 != null) {
            menuCombo1.getItems().clear();
            menuCombo1.getItems().addAll(controller.getAllMenus());
        }
        
        if (menuCombo2 != null) {
            menuCombo2.getItems().clear();
            menuCombo2.getItems().addAll(controller.getAllMenus());
        }
        
        if (sectionCombo1 != null) {
            sectionCombo1.getItems().clear();
            sectionCombo1.getItems().addAll(controller.getAllSections());
        }
        
        if (sectionCombo2 != null) {
            sectionCombo2.getItems().clear();
            sectionCombo2.getItems().addAll(controller.getAllSections());
        }
        
        if (recipeCombo != null) {
            recipeCombo.getItems().clear();
            recipeCombo.getItems().addAll(controller.getAllRecipes());
        }
    }
}