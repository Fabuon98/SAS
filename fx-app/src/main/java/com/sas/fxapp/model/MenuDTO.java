package com.sas.fxapp.model;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {
    private Long id;
    private String nome;
    private List<Long> sezioniId = new ArrayList<>();

    public MenuDTO() {}

    public MenuDTO(Long id, String nome, List<Long> sezioniId) {
        this.id = id;
        this.nome = nome;
        this.sezioniId = sezioniId;
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

    public List<Long> getSezioniId() {
        return sezioniId;
    }

    public void setSezioniId(List<Long> sezioniId) {
        this.sezioniId = sezioniId;
    }
}
