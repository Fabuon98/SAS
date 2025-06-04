package com.sas.fxapp.model;

import java.util.ArrayList;
import java.util.List;

public class RicettaDTO {
    private Long id;
    private String nome;
    private List<Long> preparazioniId = new ArrayList<>();

    public RicettaDTO() {}

    public RicettaDTO(Long id, String nome, List<Long> preparazioniId) {
        this.id = id;
        this.nome = nome;
        this.preparazioniId = preparazioniId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getPreparazioniId() {
        return preparazioniId;
    }

    public void setPreparazioniId(List<Long> preparazioniId) {
        this.preparazioniId = preparazioniId;
    }
}
