package com.auth.auth.services.interfaces;

import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Usuario;

public interface RegistroUsuarioService {
    UsuarioResponse registrarUsuario(Usuario usuario);
    UsuarioResponse registrarUsuarioFunc(UsuarioRequest usuarioRequest);
}
