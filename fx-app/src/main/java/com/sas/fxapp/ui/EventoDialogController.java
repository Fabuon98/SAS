package com.sas.fxapp.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sas.fxapp.model.EventoDTO;
import com.sas.fxapp.model.MenuDTO;
import com.sas.fxapp.model.SezioneDTO;
import com.sas.fxapp.persistence.DataService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventoDialogController {

    @FXML private TextField tfNome;
    @FXML private DatePicker dpData;

    @FXML private ComboBox<MenuDTO> cmbMenu;
    @FXML private TextField tfMenuNome;
    @FXML private Button btnDeleteMenu;    // pulsante per eliminare il menu selezionato
    @FXML private TableView<SezioneDTO> sezioniTable;
    @FXML private TableColumn<SezioneDTO, Long>   colSezId;
    @FXML private TableColumn<SezioneDTO, String> colSezNome;

    private Stage dialogStage;
    private EventoDTO evento;
    private boolean okClicked = false;
    private final DataService data = new DataService();
    private MenuDTO menuCorrente;

    // Lista osservabile di tutti i menu salvati
    private ObservableList<MenuDTO> menuList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 1) Colonne della tabella Sezioni
        colSezId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSezNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // 2) Carico tutti i menu da file e popolo la ComboBox
        List<MenuDTO> tuttiMenu = data.findAllMenu();
        menuList.setAll(tuttiMenu);
        cmbMenu.setItems(menuList);

        // 3) Configuro come mostrare il nome dei menu nella ComboBox
        cmbMenu.setCellFactory(lv -> new ListCell<MenuDTO>() {
            @Override
            protected void updateItem(MenuDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        cmbMenu.setButtonCell(new ListCell<MenuDTO>() {
            @Override
            protected void updateItem(MenuDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });

        // 4) Listener sulla selezione: carico il menuCorrente e le relative sezioni
        cmbMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                menuCorrente = newVal;
                tfMenuNome.setText(menuCorrente.getNome());
                caricaSezioni(menuCorrente);
                btnDeleteMenu.setDisable(false);
            } else {
                tfMenuNome.clear();
                sezioniTable.getItems().clear();
                btnDeleteMenu.setDisable(true);
            }
        });

        // 5) Disabilito di default il pulsante “Elimina”
        btnDeleteMenu.setDisable(true);
        // (ATTENZIONE: NON disabilitare la ComboBox stessa!)
        cmbMenu.setDisable(false);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /** Popola i campi se stai modificando un evento esistente */
    public void setEvento(EventoDTO evento) {
        this.evento = evento;

        tfNome.setText(evento.getNome());
        dpData.setValue(evento.getData());

        // Se l'evento ha già un menuId, seleziono quel menu
        if (evento.getMenuId() != null) {
            Optional<MenuDTO> opt = data.findMenuById(evento.getMenuId());
            if (opt.isPresent()) {
                MenuDTO trovato = opt.get();

                // Cerco nella lista un elemento con lo stesso ID:
                MenuDTO esistente = menuList.stream()
                                            .filter(m -> m.getId().equals(trovato.getId()))
                                            .findFirst()
                                            .orElse(null);

                if (esistente != null) {
                    // Se lo trovo, imposto menuCorrente su quell'istanza
                    menuCorrente = esistente;
                } else {
                    // Altrimenti lo aggiungo (è un menu nuovo)
                    menuCorrente = trovato;
                    menuList.add(menuCorrente);
                }

                // Seleziono in ComboBox l’istanza corretta (esistente o nuova)
                cmbMenu.getSelectionModel().select(menuCorrente);
                tfMenuNome.setText(menuCorrente.getNome());
                caricaSezioni(menuCorrente);
                btnDeleteMenu.setDisable(false);
            }
        }
    }

    private void caricaSezioni(MenuDTO menu) {
        List<SezioneDTO> sezioni = new ArrayList<>();
        for (Long idSez : menu.getSezioniId()) {
            data.findSezioneById(idSez).ifPresent(sezioni::add);
        }
        sezioniTable.setItems(FXCollections.observableArrayList(sezioni));
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        // Salva nome e data dell'evento
        evento.setNome(tfNome.getText());
        evento.setData(dpData.getValue());

        // Verifica che un menu sia selezionato
        MenuDTO selezionato = cmbMenu.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            dialogStage.close();
            return;
        }
        menuCorrente = selezionato;

        // Aggiorna il nome del menu e salva
        menuCorrente.setNome(tfMenuNome.getText());
        data.saveMenu(menuCorrente);

        // Salva ciascuna sezione presente nella tabella (una sola volta ciascuna)
        List<SezioneDTO> sezioniCorrenti = sezioniTable.getItems();
        List<Long> idsSezioni = new ArrayList<>();
        for (SezioneDTO s : sezioniCorrenti) {
            if (s.getId() == null) {
                Long newSezId = data.nextSezioneId();
                s.setId(newSezId);
            }
            data.saveSezione(s);
            idsSezioni.add(s.getId());
        }
        // Aggiorna lista di sezioniId nel menu e salva
        menuCorrente.setSezioniId(idsSezioni);
        data.saveMenu(menuCorrente);

        // Imposta menuId nell'evento e salva l'evento
        evento.setMenuId(menuCorrente.getId());
        data.saveEvento(evento);

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    @FXML
    private void onNewMenu() {
        // Crea un nuovo menu con nome di default
        MenuDTO nuovoMenu = new MenuDTO();
        nuovoMenu.setNome("Menu generico");
        Long id = data.nextMenuId();
        nuovoMenu.setId(id);
        data.saveMenu(nuovoMenu);

        // Aggiungi alla lista e selezionalo
        menuList.add(nuovoMenu);
        cmbMenu.getSelectionModel().select(nuovoMenu);
        tfMenuNome.setText(nuovoMenu.getNome());

        // Svuota tabella sezioni (nessuna ancora creata)
        sezioniTable.getItems().clear();
        btnDeleteMenu.setDisable(false);
    }

    @FXML
    private void onAddSezione() throws IOException {
        if (menuCorrente == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SezioneDialog.fxml"));
        Parent page = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Nuova Sezione");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dialogStage);
        stage.setScene(new Scene(page));

        SezioneDialogController ctrl = loader.getController();
        SezioneDTO nuova = new SezioneDTO();
        ctrl.setSezione(nuova);
        ctrl.setDialogStage(stage);

        stage.showAndWait();
        if (ctrl.isOkClicked()) {
            // Assegniamo l’ID e salviamo UNA SOLA VOLTA
            Long newSezId = data.nextSezioneId();
            nuova.setId(newSezId);
            data.saveSezione(nuova);

            menuCorrente.getSezioniId().add(newSezId);
            data.saveMenu(menuCorrente);

            // Aggiunge la nuova sezione in tabella
            ObservableList<SezioneDTO> current = sezioniTable.getItems();
            current.add(nuova);
            sezioniTable.setItems(current);
        }
    }

    @FXML
    private void onEditSezione() throws IOException {
        SezioneDTO sel = sezioniTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SezioneDialog.fxml"));
        Parent page = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Modifica Sezione");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(dialogStage);
        stage.setScene(new Scene(page));

        SezioneDialogController ctrl = loader.getController();
        ctrl.setSezione(sel);
        ctrl.setDialogStage(stage);

        stage.showAndWait();
        if (ctrl.isOkClicked()) {
            data.saveSezione(sel);
            sezioniTable.refresh();
        }
    }

    @FXML
    private void onDeleteSezione() {
        SezioneDTO sel = sezioniTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            data.deleteSezione(sel.getId());
            menuCorrente.getSezioniId().remove(sel.getId());
            data.saveMenu(menuCorrente);

            // Rimuove solo la sezione selezionata dalla tabella
            ObservableList<SezioneDTO> current = sezioniTable.getItems();
            current.remove(sel);
            sezioniTable.setItems(current);
        }
    }

    /** 
     * Nuovo metodo per eliminare il Menu selezionato.
     */
    @FXML
    private void onDeleteMenu() {
        MenuDTO selMenu = cmbMenu.getSelectionModel().getSelectedItem();
        if (selMenu == null) {
            return; // Non c'è nulla da cancellare
        }

        // 1) Rimuovo dal JSON
        data.deleteMenu(selMenu.getId());

        // 2) Rimuovo dalla lista in memoria e aggiorno la UI
        menuList.remove(selMenu);
        cmbMenu.getSelectionModel().clearSelection();
        tfMenuNome.clear();
        sezioniTable.getItems().clear();
        btnDeleteMenu.setDisable(true);
    }
}
