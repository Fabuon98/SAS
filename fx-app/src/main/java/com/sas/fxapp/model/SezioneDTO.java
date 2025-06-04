package com.sas.fxapp.model;

import java.util.ArrayList;
import java.util.List;

public class SezioneDTO {
    private Long id;
    private String nome;
    private List<Long> ricetteId = new ArrayList<>();

    public SezioneDTO() {}

    public SezioneDTO(Long id, String nome, List<Long> ricetteId) {
        this.id = id;
        this.nome = nome;
        this.ricetteId = ricetteId;
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

    public List<Long> getRicetteId() {
        return ricetteId;
    }

    public void setRicetteId(List<Long> ricetteId) {
        this.ricetteId = ricetteId;
    }
}
