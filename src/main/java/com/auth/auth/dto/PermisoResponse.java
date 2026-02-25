package com.auth.auth.dto;

public class PermisoResponse {
    private Long id;
    private String nombre;
    private String nombreModulo;

    public PermisoResponse(Long id, String nombre, String nombreModulo) {
        this.id = id;
        this.nombre = nombre;
        this.nombreModulo = nombreModulo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }
}