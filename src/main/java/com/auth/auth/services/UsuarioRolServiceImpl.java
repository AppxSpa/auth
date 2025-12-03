package com.auth.auth.services;

import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.RolService;
import com.auth.auth.services.interfaces.UsuarioRolService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    private final RolService rolService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioRolServiceImpl(RolService rolService, UsuarioRepository usuarioRepository) {
        this.rolService = rolService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Rol> getRolesParaUsuario(boolean isAdmin, boolean isFunc) {
        List<Rol> roles = new ArrayList<>();
        rolService.getByName("ROLE_USER").ifPresent(roles::add);
        if (isAdmin) {
            rolService.getByName("ROLE_ADMIN").ifPresent(roles::add);
        }
        if (isFunc) {
            rolService.getByName("ROLE_FUNC").ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public void actualizarRoles(Usuario usuario, List<Rol> newRoles) {
        Set<Rol> uniqueRoles = new HashSet<>(usuario.getRoles());
        uniqueRoles.addAll(newRoles);
        usuario.setRoles(new ArrayList<>(uniqueRoles));
        usuarioRepository.save(usuario);
    }
}
