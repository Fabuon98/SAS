package com.sas.fxapp.model;

public class PreparazioneDTO {
    private Long id;
    private String descrizione;

    public PreparazioneDTO() {}

    public PreparazioneDTO(Long id, String descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
