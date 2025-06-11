// RecipeWithNewNameDialog.java - Dialog per collegare una ricetta esistente a una sezione con un nuovo nome
// Posizione: src/main/java/com/catering/view/fx/RecipeWithNewNameDialog.java

package com.catering.view.fx;

import com.catering.controller.ChefController;   // Controller per operazioni CRUD su Ricette e Sezioni
import com.catering.model.Recipe;               // Modello di dominio per Ricetta
import com.catering.model.Section;              // Modello di dominio per Sezione
import com.catering.model.Preparation;          // Modello di dominio per singolo passaggio di preparazione
import javafx.geometry.Insets;                  // Per padding nei layout
import javafx.scene.control.*;                  // Dialog, Label, TextField, TextArea, Separator, ButtonType, Alert
import javafx.scene.layout.VBox;                // Layout verticale per comporre i nodi

/**
 * Dialog per collegare una ricetta a una sezione, eventualmente rinominandola.
 * Estende Dialog<Boolean>:
 *  - restituisce true se l’operazione va a buon fine,
 *  - false se annullata o in caso di errore.
 */
public class RecipeWithNewNameDialog extends Dialog<Boolean> {
    private final ChefController controller;      // Controller per interagire con il modello
    private final Section section;                // Sezione di destinazione
    private final Recipe originalRecipe;          // Ricetta di partenza da collegare

    // Campo di input per il nuovo nome della ricetta
    private TextField newNameField;

    /**
     * Costruttore:
     * @param controller      istanza di ChefController per le operazioni di creazione/collegamento
     * @param section         sezione a cui collegare la ricetta
     * @param originalRecipe  ricetta originale da rinominare o semplicemente collegare
     */
    public RecipeWithNewNameDialog(ChefController controller, Section section, Recipe originalRecipe) {
        this.controller = controller;
        this.section = section;
        this.originalRecipe = originalRecipe;
        setupDialog();  // Configurazione dell’interfaccia e dei comportamenti
    }

    /**
     * Configura titolo, header, contenuto grafico e risultato del dialog.
     */
    private void setupDialog() {
        // Titolo e testo dell’header dinamici, includendo i nomi delle entità
        setTitle("Collega Ricetta con Nuovo Nome");
        setHeaderText(
            "Collega \"" + originalRecipe.getName() + "\" alla sezione \"" 
            + section.getName() + "\" con un nuovo nome"
        );

        // Layout verticale con spaziatura e padding
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Label che mostra il nome della ricetta originale in grassetto
        Label originalInfo = new Label("Ricetta originale: " + originalRecipe.getName());
        originalInfo.setStyle("-fx-font-weight: bold;");

        // Campo per inserire il nuovo nome, precompilato con il nome originale
        newNameField = new TextField(originalRecipe.getName());
        newNameField.setPromptText("Inserisci il nuovo nome per questa ricetta...");

        // Label e TextArea che elencano i dettagli che rimangono invariati
        Label detailsLabel = new Label("Dettagli che verranno mantenuti:");
        detailsLabel.setStyle("-fx-font-weight: bold;");

        TextArea detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(6);

        // Costruzione del testo riepilogativo dei campi della ricetta originale
        StringBuilder details = new StringBuilder();
        details.append("Descrizione: ").append(originalRecipe.getDescription()).append("\n");
        details.append("Tempo preparazione: ").append(originalRecipe.getPreparationTime()).append(" min\n");
        details.append("Tempo cottura: ").append(originalRecipe.getCookingTime()).append(" min\n");
        details.append("Porzioni: ").append(originalRecipe.getServings()).append("\n");
        details.append("Difficoltà: ").append(originalRecipe.getDifficulty()).append("\n");
        details.append("Ingredienti: ").append(originalRecipe.getIngredients().size()).append(" elementi\n");
        details.append("Preparazioni: ").append(originalRecipe.getPreparations().size()).append(" passaggi");

        detailsArea.setText(details.toString());

        // Assemblaggio dei nodi nel VBox
        content.getChildren().addAll(
            originalInfo,
            new Separator(),
            new Label("Nuovo nome:"),
            newNameField,
            new Separator(),
            detailsLabel,
            detailsArea
        );

        // Imposta il contenuto del DialogPane e i bottoni OK/Cancel
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Converter per trasformare il bottone premuto in un Boolean e lanciare la logica
        setResultConverter(this::processResult);
    }

    /**
     * Processa il risultato premuto dall’utente:
     * - Se OK, valida il nuovo nome, crea una copia se necessario
     *   e collega la ricetta alla sezione,
     *   mostrando messaggi di successo o errore.
     * - Restituisce true in caso di successo, false altrimenti.
     */
    private Boolean processResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            String newName = newNameField.getText().trim();

            // Validazione minima: il nome non può essere vuoto
            if (newName.isEmpty()) {
                AlertUtils.showError("Il nome non può essere vuoto.");
                return false;
            }

            try {
                if (!newName.equals(originalRecipe.getName())) {
                    // Se il nuovo nome differisce, crea una copia della ricetta
                    Recipe copy = createRecipeCopy(originalRecipe, newName);
                    // Collega la nuova ricetta alla sezione
                    controller.addRecipeToSection(section.getId(), copy.getId());
                    AlertUtils.showSuccess(
                        "Ricetta \"" + newName + "\" creata e collegata alla sezione \"" 
                        + section.getName() + "\"!\n" +
                        "La ricetta originale \"" + originalRecipe.getName() + "\" rimane invariata."
                    );
                } else {
                    // Se il nome è uguale, collega direttamente l’originale
                    controller.addRecipeToSection(section.getId(), originalRecipe.getId());
                    AlertUtils.showSuccess("Ricetta collegata alla sezione con successo!");
                }
                return true;

            } catch (Exception e) {
                AlertUtils.showError("Errore durante il collegamento: " + e.getMessage());
                return false;
            }
        }
        // Se CANCEL o altre azioni, ritorna false
        return false;
    }

    /**
     * Crea una copia di una Recipe esistente con un nuovo nome,
     * copiando descrizione, tempi, porzioni, difficoltà, ingredienti e preparazioni.
     * @param original ricetta di partenza
     * @param newName  nuovo nome da assegnare
     * @return la nuova istanza di Recipe creata
     */
    private Recipe createRecipeCopy(Recipe original, String newName) {
        // Creazione di base con nome e descrizione
        Recipe copy = controller.createRecipe(newName, original.getDescription());

        // Copia dei dettagli numerici e categorici
        copy.setPreparationTime(original.getPreparationTime());
        copy.setCookingTime(original.getCookingTime());
        copy.setServings(original.getServings());
        copy.setDifficulty(original.getDifficulty());

        // Copia degli ingredienti uno per uno
        for (String ingredient : original.getIngredients()) {
            copy.addIngredient(ingredient);
        }

        // Copia delle preparazioni utilizzando il controller per mantenere eventuale logica aggiuntiva
        for (Preparation prep : original.getPreparations()) {
            Preparation newPrep = controller.createPreparation(
                prep.getStepNumber(),
                prep.getDescription(),
                prep.getTimeInMinutes()
            );
            copy.addPreparation(newPrep);
        }

        return copy;
    }
}
