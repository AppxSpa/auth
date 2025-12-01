package com.auth.auth.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "perfiles")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "perfiles", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sistema_id", nullable = false)
    private Sistema sistema;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "perfiles_modulos", joinColumns = @JoinColumn(name = "perfil_id"), inverseJoinColumns = @JoinColumn(name = "modulo_id"))
    private Set<Modulo> modulos = new HashSet<>();

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

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void addUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public Sistema getSistema() {
        return sistema;
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public Set<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(Set<Modulo> modulos) {
        this.modulos = modulos;
    }

    public void addModulo(Modulo modulo) {
        modulos.add(modulo);
    }

    public void removeModulo(Modulo modulo) {
        modulos.remove(modulo);
    }

}
