// ChefViewFX.java - Classe principale coordinatrice
package com.catering.view.fx;

import com.catering.controller.ChefController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ChefViewFX extends Application {
    private ChefController controller;
    private TabPane tabPane;
    
    // Istanze delle view delle tab
    private EventsTabView eventsTabView;
    private MenusTabView menusTabView;
    private SectionsTabView sectionsTabView;
    private RecipesTabView recipesTabView;
    private ConnectionsTabView connectionsTabView;
    
    @Override
    public void start(Stage primaryStage) {
        controller = new ChefController();
        
        // Inizializza le view delle tab
        eventsTabView = new EventsTabView(controller);
        menusTabView = new MenusTabView(controller);
        sectionsTabView = new SectionsTabView(controller);
        recipesTabView = new RecipesTabView(controller);
        connectionsTabView = new ConnectionsTabView(controller);
        
        primaryStage.setTitle("Gestione Chef - Applicazione per Eventi e Menu");
        
        // Crea il TabPane principale
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            eventsTabView.createTab(),
            menusTabView.createTab(),
            sectionsTabView.createTab(),
            recipesTabView.createTab(),
            connectionsTabView.createTab()
        );
        
        // Listener per aggiornare le ComboBox quando cambi tab
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().equals("Collegamenti")) {
                connectionsTabView.refreshComboBoxes();
            }
        });
        
        primaryStage.setScene(new Scene(tabPane, 1000, 650));
        primaryStage.show();
        
        // Carica i dati iniziali in tutte le tab
        refreshAllTabs();
    }
    
    private void refreshAllTabs() {
        eventsTabView.refreshData();
        menusTabView.refreshData();
        sectionsTabView.refreshData();
        recipesTabView.refreshData();
        connectionsTabView.refreshComboBoxes();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}