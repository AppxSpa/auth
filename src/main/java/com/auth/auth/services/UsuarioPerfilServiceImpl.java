package com.auth.auth.services;

import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioSistemaPerfil;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.UsuarioPerfilService;
import com.auth.auth.utils.RepositoryUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

        usuario.getAccesosSistemas().clear();
        for (Perfil perfil : perfiles) {
            UsuarioSistemaPerfil acceso = new UsuarioSistemaPerfil();
            acceso.setUsuario(usuario);
            acceso.setPerfil(perfil);
            usuario.getAccesosSistemas().add(acceso);
        }
        usuarioRepository.save(usuario);
    }

    @Override
    public void agregarPerfilAUsuario(String username, Long perfilId) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));

        Perfil perfil = RepositoryUtils.findOrThrow(perfilRepository.findById(perfilId),
                String.format(PROFILE_NOT_FOUND, perfilId));

        Set<UsuarioSistemaPerfil> accesosActuales = usuario.getAccesosSistemas();
        boolean existe = accesosActuales.stream().anyMatch(acceso -> acceso.getPerfil().getId().equals(perfil.getId()));
        if (!existe) {
            UsuarioSistemaPerfil nuevoAcceso = new UsuarioSistemaPerfil();
            nuevoAcceso.setUsuario(usuario);
            nuevoAcceso.setPerfil(perfil);
            accesosActuales.add(nuevoAcceso);
            usuarioRepository.save(usuario);
        }
    }

    @Override
    public void removerPerfilDeUsuario(String username, Long perfilId) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));

        Set<UsuarioSistemaPerfil> accesosActuales = usuario.getAccesosSistemas();

        boolean removed = accesosActuales.removeIf(acceso -> acceso.getPerfil().getId().equals(perfilId));
        if (removed) {
            usuarioRepository.save(usuario);
        }
    }
}
