package com.auth.auth.dto;

public class DepartamentoResponse {

    private Long id;
    private String nombre;
    private Long codDEptoSuperior;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCodDEptoSuperior() {
        return codDEptoSuperior;
    }

    public void setCodDEptoSuperior(Long codDEptoSuperior) {
        this.codDEptoSuperior = codDEptoSuperior;
    }

}
