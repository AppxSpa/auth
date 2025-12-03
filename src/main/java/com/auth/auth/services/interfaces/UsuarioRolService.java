package com.auth.auth.services.interfaces;

import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import java.util.List;

public interface UsuarioRolService {
    List<Rol> getRolesParaUsuario(boolean isAdmin, boolean isFunc);
    void actualizarRoles(Usuario usuario, List<Rol> newRoles);
}
