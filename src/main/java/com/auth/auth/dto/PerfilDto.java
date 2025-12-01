package com.auth.auth.dto;

public class PerfilDto {

    private Long id;
    private String nombre;
    private SistemaDto sistema;
    private java.util.Set<ModuloDto> modulos;

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

    public SistemaDto getSistema() {
        return sistema;
    }

    public void setSistema(SistemaDto sistema) {
        this.sistema = sistema;
    }

    public java.util.Set<ModuloDto> getModulos() {
        return modulos;
    }

    public void setModulos(java.util.Set<ModuloDto> modulos) {
        this.modulos = modulos;
    }
}
