package com.auth.auth.services.interfaces;

import java.util.List;

public interface UsuarioPerfilService {
    void asignarPerfilesAUsuario(String username, List<Long> perfilIds);
    void agregarPerfilAUsuario(String username, Long perfilId);
    void removerPerfilDeUsuario(String username, Long perfilId);
}
