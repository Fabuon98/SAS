package com.sas.fxapp.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import com.sas.fxapp.model.EventoDTO;
import com.sas.fxapp.persistence.DataService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private TableView<EventoDTO> eventiTable;
    @FXML private TableColumn<EventoDTO, Long>      colId;
    @FXML private TableColumn<EventoDTO, String>    colNome;
    @FXML private TableColumn<EventoDTO, LocalDate> colData;

    private final DataService data = new DataService();

    @FXML
    public void initialize() {
        // Imposto le colonne della tabella eventi
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));

        // Carico tutti gli eventi da file e popolo la TableView
        ObservableList<EventoDTO> items =
            FXCollections.observableArrayList(data.findAllEventi());
        eventiTable.setItems(items);
    }

    @FXML
    public void onAddEvento() {
        try {
            // Creo un EventoDTO vuoto (menuId resterà null fino a selezione nel dialog)
            EventoDTO nuovoEvento = new EventoDTO();

            // Carico il dialog per l'evento
            URL fxmlUrl = getClass().getResource("/fxml/EventoDialog.fxml");
            if (fxmlUrl == null) {
                System.err.println("ERRORE: /fxml/EventoDialog.fxml non trovato!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent page = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Nuovo Evento");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(eventiTable.getScene().getWindow());
            dialog.setScene(new Scene(page));

            EventoDialogController ctrl = loader.getController();
            ctrl.setEvento(nuovoEvento);
            ctrl.setDialogStage(dialog);

            dialog.showAndWait();
            if (ctrl.isOkClicked()) {
                // Se l'utente ha premuto OK (e ha selezionato/creato un menu), salvo l'evento
                data.saveEvento(nuovoEvento);
                eventiTable.getItems().add(nuovoEvento);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditEvento() {
        EventoDTO sel = eventiTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            System.err.println("Nessun evento selezionato!");
            return;
        }

        try {
            URL fxmlUrl = getClass().getResource("/fxml/EventoDialog.fxml");
            if (fxmlUrl == null) {
                System.err.println("ERRORE: /fxml/EventoDialog.fxml non trovato!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent page = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Modifica Evento");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(eventiTable.getScene().getWindow());
            dialog.setScene(new Scene(page));

            EventoDialogController ctrl = loader.getController();
            ctrl.setEvento(sel);
            ctrl.setDialogStage(dialog);

            dialog.showAndWait();
            if (ctrl.isOkClicked()) {
                data.saveEvento(sel);
                eventiTable.refresh();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteEvento() {
        EventoDTO sel = eventiTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            data.deleteEvento(sel.getId());
            eventiTable.getItems().remove(sel);
        }
    }
}
