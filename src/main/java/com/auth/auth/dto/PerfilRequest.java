package com.auth.auth.dto;

public class PerfilRequest {
    private String nombre;
    private Long sistemaId;
    private java.util.Set<Long> moduloIds;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(Long sistemaId) {
        this.sistemaId = sistemaId;
    }

    public java.util.Set<Long> getModuloIds() {
        return moduloIds;
    }

    public void setModuloIds(java.util.Set<Long> moduloIds) {
        this.moduloIds = moduloIds;
    }
}
