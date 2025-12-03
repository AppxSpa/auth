package com.auth.auth.services;

import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.UsuarioPerfilService;
import com.auth.auth.utils.RepositoryUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPerfilServiceImpl implements UsuarioPerfilService {

    private static final String USER_NOT_FOUND = "Usuario %s no encontrado";
    private static final String PROFILE_NOT_FOUND = "Perfil %d no encontrado";

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    public UsuarioPerfilServiceImpl(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Override
    public void asignarPerfilesAUsuario(String username, List<Long> perfilIds) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));

        List<Perfil> perfiles = perfilRepository.findAllById(perfilIds);
        if (perfiles.size() != perfilIds.size()) {
            throw new IllegalArgumentException("Alguno de los perfiles no fue encontrado");
        }

        usuario.setPerfiles(perfiles);
        usuarioRepository.save(usuario);
    }

    @Override
    public void agregarPerfilAUsuario(String username, Long perfilId) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));

        Perfil perfil = RepositoryUtils.findOrThrow(perfilRepository.findById(perfilId),
                String.format(PROFILE_NOT_FOUND, perfilId));

        List<Perfil> actuales = usuario.getPerfiles();
        if (actuales == null) {
            actuales = new java.util.ArrayList<>();
        }
        boolean existe = actuales.stream().anyMatch(p -> p.getId().equals(perfil.getId()));
        if (!existe) {
            actuales.add(perfil);
            usuario.setPerfiles(actuales);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void removerPerfilDeUsuario(String username, Long perfilId) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));

        List<Perfil> actuales = usuario.getPerfiles();
        if (actuales == null || actuales.isEmpty()) {
            return;
        }

        boolean removed = actuales.removeIf(p -> p.getId().equals(perfilId));
        if (removed) {
            usuario.setPerfiles(actuales);
            usuarioRepository.save(usuario);
        }
    }
}
