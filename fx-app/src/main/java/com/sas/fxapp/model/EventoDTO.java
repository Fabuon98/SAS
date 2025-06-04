package com.sas.fxapp.model;

import java.time.LocalDate;

public class EventoDTO {
    private Long id;
    private String nome;
    private LocalDate data;
    private Long menuId;

    public EventoDTO() {}

    public EventoDTO(Long id, String nome, LocalDate data, Long menuId) {
        this.id = id;
        this.nome = nome;
        this.data = data;
        this.menuId = menuId;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
