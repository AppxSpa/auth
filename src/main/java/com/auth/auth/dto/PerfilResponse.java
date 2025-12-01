package com.auth.auth.dto;

public class PerfilResponse {
    private Long id;
    private String nombre;
    private String sistema;
    private java.util.Set<String> modulos;

    public PerfilResponse(Long id, String nombre, String sistema) {
        this.id = id;
        this.nombre = nombre;
        this.sistema = sistema;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSistema() {
        return sistema;
    }

    public java.util.Set<String> getModulos() {
        return modulos;
    }

    public void setModulos(java.util.Set<String> modulos) {
        this.modulos = modulos;
    }
}
