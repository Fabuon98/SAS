package com.sas.fxapp.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto d’ingresso dell’applicazione JavaFX.
 * Estende javafx.application.Application, che gestisce il ciclo di vita dell’app.
 */
public class App extends Application {

    /**
     * Metodo invocato da JavaFX all’avvio dell’applicazione.
     * Qui carichiamo il layout FXML, lo inseriamo in una Scene e mostriamo la finestra principale.
     *
     * @param primaryStage il palco principale su cui impostare la Scene
     * @throws Exception in caso di problemi nel caricamento del FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML che definisce la UI principale (Main.fxml nella cartella resources/fxml)
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/Main.fxml")
        );

        // Crea una nuova Scene passando il nodo radice caricato dal FXML
        Scene scene = new Scene(loader.load());

        // Imposta la Scene sullo Stage principale
        primaryStage.setScene(scene);

        // Titolo della finestra
        primaryStage.setTitle("Gestione Eventi");

        // Mostra la finestra all’utente
        primaryStage.show();
    }

    /**
     * Metodo main standard: lancia il framework JavaFX.
     * launch() intestisce il thread JavaFX e poi chiama start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
