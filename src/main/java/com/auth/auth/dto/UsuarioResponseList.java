package com.auth.auth.dto;

public class UsuarioResponseList {

    private String username;
    private String nombre;
    private Long idDepto;
    private Integer rut;
    private String vrut;
    private String nombreDepartamento;

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }
    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Integer getRut() {
        return rut;
    }

    public void setRut(Integer rut) {
        this.rut = rut;
    }

    public String getVrut() {
        return vrut;
    }

    public void setVrut(String vrut) {
        this.vrut = vrut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdDepto() {
        return idDepto;
    }

    public void setIdDepto(Long idDpeto) {
        this.idDepto = idDpeto;
    }

}
