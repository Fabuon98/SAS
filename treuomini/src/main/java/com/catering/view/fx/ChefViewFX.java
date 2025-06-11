// ChefViewFX.java - Classe principale coordinatrice dell'interfaccia JavaFX
package com.catering.view.fx;

import com.catering.controller.ChefController;    // Controller centrale per la logica di business

import javafx.application.Application;            // Classe base per le applicazioni JavaFX
import javafx.scene.Scene;                       // Contenitore di base per la UI
import javafx.scene.control.TabPane;             // Controllo per gestire un insieme di schede
import javafx.stage.Stage;                       // Finestra primaria dell'applicazione

/**
 * ChefViewFX è il punto di ingresso dell'interfaccia grafica JavaFX.
 * Estende Application e definisce il layout a schede (TabPane),
 * collegando ogni scheda a una view specifica (Events, Menus, Sections, Recipes, Connections).
 */
public class ChefViewFX extends Application {
    // Riferimento al controller che gestisce tutti i dati e la logica
    private ChefController controller;
    // Controllo principale: un TabPane che contiene tutte le schede
    private TabPane tabPane;
    
    // Istanze delle singole view/tab, ognuna responsabile di una sezione funzionale
    private EventsTabView eventsTabView;
    private MenusTabView menusTabView;
    private SectionsTabView sectionsTabView;
    private RecipesTabView recipesTabView;
    private ConnectionsTabView connectionsTabView;
    
    /**
     * Metodo invocato da JavaFX all'avvio dell'applicazione.
     * Qui viene costruita tutta la UI, istanziato il controller e mostrate le schede.
     */
    @Override
    public void start(Stage primaryStage) {
        // Inizializza il controller che contiene i dati in memoria e i metodi CRUD
        controller = new ChefController();
        
        // Inizializza le view delle tab passando il controller per accedere ai dati
        eventsTabView       = new EventsTabView(controller);
        menusTabView        = new MenusTabView(controller);
        sectionsTabView     = new SectionsTabView(controller);
        recipesTabView      = new RecipesTabView(controller);
        connectionsTabView  = new ConnectionsTabView(controller);
        
        // Imposta il titolo della finestra principale
        primaryStage.setTitle("Gestione Chef - Applicazione per Eventi e Menu");
        
        // Crea il TabPane e aggiunge tutte le schede generate dalle view
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
            eventsTabView.createTab(),
            menusTabView.createTab(),
            sectionsTabView.createTab(),
            recipesTabView.createTab(),
            connectionsTabView.createTab()
        );
        
        // Listener: quando viene selezionata la scheda "Collegamenti", aggiorna le ComboBox
        // in modo che mostrino i dati correnti di eventi, menu, sezioni e ricette.
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && "Collegamenti".equals(newTab.getText())) {
                connectionsTabView.refreshComboBoxes();
            }
        });
        
        // Crea la scena principale con dimensioni di default e la associa allo Stage
        primaryStage.setScene(new Scene(tabPane, 1000, 650));
        primaryStage.show();  // Mostra la finestra
        
        // Carica subito i dati iniziali in tutte le tab per popolare le tabelle e i controlli
        refreshAllTabs();
    }
    
    /**
     * Utility method per aggiornare tutti i dati nelle view:
     * - ricarica liste di eventi, menu, sezioni, ricette
     * - aggiorna le ComboBox nella tab di collegamento
     */
    private void refreshAllTabs() {
        eventsTabView.refreshData();
        menusTabView.refreshData();
        sectionsTabView.refreshData();
        recipesTabView.refreshData();
        connectionsTabView.refreshComboBoxes();
    }
    
    /**
     * Main method che avvia l'applicazione JavaFX.
     * JavaFX gestisce il thread dell'interfaccia e chiama start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
