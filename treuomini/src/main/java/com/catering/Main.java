// Main.java - Classe principale per avviare l'applicazione
// Posizione: src/main/java/com/catering/Main.java

package com.catering;  

// Import della classe JavaFX che gestisce il ciclo di vita di un’applicazione FX
import javafx.application.Application;

// Import della nostra classe che estende Application e definisce la UI
import com.catering.view.fx.ChefViewFX;

/**
 * Punto di ingresso dell’applicazione.
 * Contiene il metodo main() che la JVM invoca per avviare il programma.
 */
public class Main {

    /**
     * Metodo main: entry point standard di qualunque applicazione Java.
     * 
     * @param args Parametri da linea di comando (eventualmente passati all’app JavaFX)
     */
    public static void main(String[] args) {
        // Invochiamo Application.launch per:
        // 1. Inizializzare l’ambiente JavaFX (thread grafico, toolkit, ecc.)
        // 2. Creare un'istanza di ChefViewFX
        // 3. Chiamare init() e start(Stage) definiti in ChefViewFX
        // 4. Gestire il ciclo di vita fino alla chiusura con stop()
        Application.launch(ChefViewFX.class, args);
    }
}
