package com.sas.fxapp.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sas.fxapp.model.RicettaDTO;
import com.sas.fxapp.model.SezioneDTO;
import com.sas.fxapp.persistence.DataService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SezioneDialogController {

    @FXML private TextField tfSezNome;

    @FXML private TableView<RicettaDTO> ricetteTable;
    @FXML private TableColumn<RicettaDTO, Long>   colRicId;
    @FXML private TableColumn<RicettaDTO, String> colRicNome;

    private Stage dialogStage;
    private SezioneDTO sezione;
    private boolean okClicked = false;
    private final DataService data = new DataService();

    @FXML
    private void initialize() {
        colRicId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRicNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSezione(SezioneDTO sezione) {
        this.sezione = sezione;
        tfSezNome.setText(sezione.getNome());

        // Carico in memoria le RicettaDTO riferite a questa sezione
        List<RicettaDTO> ricette = new ArrayList<>();
        for (Long idRic : sezione.getRicetteId()) {
            data.findRicettaById(idRic).ifPresent(ricette::add);
        }
        ObservableList<RicettaDTO> items =
            FXCollections.observableArrayList(ricette);
        ricetteTable.setItems(items);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        sezione.setNome(tfSezNome.getText());

        // Aggiorno la lista di ricetteId dalla tabella
        List<RicettaDTO> currentRicette = ricetteTable.getItems();
        List<Long> newRicIds = new ArrayList<>();
        for (RicettaDTO r : currentRicette) {
            if (r.getId() == null) {
                Long newRicId = data.nextRicettaId();
                r.setId(newRicId);
                data.saveRicetta(r);
            } else {
                data.saveRicetta(r);
            }
            newRicIds.add(r.getId());
        }
        sezione.setRicetteId(newRicIds);

        // NOTA: non chiamiamo più data.saveSezione(sezione) qui

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    @FXML
    private void onAddRicetta() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/RicettaDialog.fxml")
        );
        Parent page = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Nuova Ricetta");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dialogStage);
        stage.setScene(new Scene(page));

        RicettaDialogController ctrl = loader.getController();
        RicettaDTO nuova = new RicettaDTO();
        ctrl.setRicetta(nuova);
        ctrl.setDialogStage(stage);

        stage.showAndWait();
        if (ctrl.isOkClicked()) {
            Long newRicId = data.nextRicettaId();
            nuova.setId(newRicId);
            data.saveRicetta(nuova);

            sezione.getRicetteId().add(newRicId);
            // NOTA: non chiamiamo più data.saveSezione(sezione) qui

            ricetteTable.getItems().add(nuova);
        }
    }

    @FXML
    private void onEditRicetta() throws IOException {
        RicettaDTO sel = ricetteTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/RicettaDialog.fxml")
        );
        Parent page = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Modifica Ricetta");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dialogStage);
        stage.setScene(new Scene(page));

        RicettaDialogController ctrl = loader.getController();
        ctrl.setRicetta(sel);
        ctrl.setDialogStage(stage);

        stage.showAndWait();
        if (ctrl.isOkClicked()) {
            data.saveRicetta(sel);
            ricetteTable.refresh();
        }
    }

    @FXML
    private void onDeleteRicetta() {
        RicettaDTO sel = ricetteTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            data.deleteRicetta(sel.getId());
            sezione.getRicetteId().remove(sel.getId());
            // NOTA: non chiamiamo più data.saveSezione(sezione) qui

            ricetteTable.getItems().remove(sel);
        }
    }
}
