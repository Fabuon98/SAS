// AlertUtils.java - Utility per gestire Alert e Dialog
package com.catering.view.fx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AlertUtils {
    
    /**
     * Mostra un messaggio di successo
     */
    public static void showSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Successo", message);
    }
    
    /**
     * Mostra un messaggio di avvertimento
     */
    public static void showWarning(String message) {
        showAlert(Alert.AlertType.WARNING, "Attenzione", message);
    }
    
    /**
     * Mostra un messaggio di errore
     */
    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Errore", message);
    }
    
    /**
     * Mostra un messaggio informativo con titolo e header personalizzati
     */
    public static void showInfo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Mostra un dialog di conferma per l'eliminazione
     * @param itemType tipo di elemento (es. "evento", "menu", "ricetta")
     * @param itemName nome dell'elemento da eliminare
     * @return true se l'utente conferma l'eliminazione
     */
    public static boolean confirmDelete(String itemType, String itemName) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Conferma Eliminazione");
        confirmation.setHeaderText("Sei sicuro di voler eliminare questo " + itemType + "?");
        confirmation.setContentText(itemType + ": " + itemName);
        
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Mostra un dialog di conferma generico
     * @param title titolo del dialog
     * @param header testo header
     * @param content contenuto del messaggio
     * @return true se l'utente clicca OK
     */
    public static boolean confirm(String title, String header, String content) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(title);
        confirmation.setHeaderText(header);
        confirmation.setContentText(content);
        
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Metodo helper privato per creare alert base
     */
    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}