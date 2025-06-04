package com.sas.fxapp.ui;

import com.sas.fxapp.model.RicettaDTO;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RicettaDialogController {

    @FXML private TextField tfRicNome;

    private Stage dialogStage;
    private RicettaDTO ricetta;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRicetta(RicettaDTO ricetta) {
        this.ricetta = ricetta;
        tfRicNome.setText(ricetta.getNome());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void onOk() {
        ricetta.setNome(tfRicNome.getText());
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
