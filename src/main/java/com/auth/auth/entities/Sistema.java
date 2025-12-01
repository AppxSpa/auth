package com.auth.auth.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sistemas")
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String codigo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sistema", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Modulo> modulos = new HashSet<>();

    @OneToMany(mappedBy = "sistema", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Perfil> perfiles = new HashSet<>();

    public Sistema() {
    }

    public Sistema(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // Getters y Setters

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Set<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(Set<Modulo> modulos) {
        this.modulos = modulos;
    }

    public Set<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Set<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public void addPerfil(Perfil perfil) {
        perfiles.add(perfil);
        perfil.setSistema(this);
    }

    public void removePerfil(Perfil perfil) {
        perfiles.remove(perfil);
        perfil.setSistema(null);
    }

    // Métodos auxiliares para mantener consistencia bidireccional

    public void addModulo(Modulo modulo) {
        modulos.add(modulo);
        modulo.setSistema(this);
    }

    public void removeModulo(Modulo modulo) {
        modulos.remove(modulo);
        modulo.setSistema(null);
    }

}
