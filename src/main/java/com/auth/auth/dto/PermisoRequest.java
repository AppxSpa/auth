package com.auth.auth.dto;

public class PermisoRequest {

    private String nombre;
    private Long sistemaId;
    private Long moduloId;
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
    public Long getModuloId() {
        return moduloId;
    }
    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

   
}
