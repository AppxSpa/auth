package com.auth.auth.services;

import java.util.List;

import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.dto.UsuarioResponseList;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;

public interface UsuarioService {

  List<UsuarioResponseList> findAll();

  UsuarioResponse createUser(Usuario usuario);

  UsuarioResponse createUserFunc(UsuarioRequest usuario);

  UsuarioResponse getUsuario(String username);

  void changeMail(ChangeMailRequest request);

  Usuario getUsuarioByPersona(Persona persona);

  Usuario save(Usuario usuario);

  Usuario findByUsername(String username);

  // Asignar una lista de perfiles a un usuario (reemplaza los perfiles actuales)
  void asignarPerfilesAUsuario(String username, java.util.List<Long> perfilIds);

  // Agregar un perfil a un usuario sin eliminar los existentes
  void agregarPerfilAUsuario(String username, Long perfilId);

  // Remover un perfil de un usuario
  void removerPerfilDeUsuario(String username, Long perfilId);

}
