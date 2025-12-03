package com.auth.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;
import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.DepartamentoResponse;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.dto.UsuarioResponseList;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.ApiDepartamentoService;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.services.interfaces.GestionCuentaService;
import com.auth.auth.services.interfaces.RegistroUsuarioService;
import com.auth.auth.services.interfaces.UsuarioDepartamentosService;
import com.auth.auth.services.interfaces.UsuarioPerfilService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String USER_NOT_FOUND = "Usuario %s no encontrado";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDepartamentosService usuarioDepartamentosService;
    private final ApiDepartamentoService apiDepartamentoService;
    private final ApiServicePersona apiServicePersona;
    private final UsuarioPerfilService usuarioPerfilService;
    private final RegistroUsuarioService registroUsuarioService;
    private final GestionCuentaService gestionCuentaService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
            UsuarioDepartamentosService usuarioDepartamentosService,
            ApiDepartamentoService apiDepartamentoService,
            ApiServicePersona apiServicePersona,
            UsuarioPerfilService usuarioPerfilService,
            RegistroUsuarioService registroUsuarioService,
            GestionCuentaService gestionCuentaService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDepartamentosService = usuarioDepartamentosService;
        this.apiDepartamentoService = apiDepartamentoService;
        this.apiServicePersona = apiServicePersona;
        this.usuarioPerfilService = usuarioPerfilService;
        this.registroUsuarioService = registroUsuarioService;
        this.gestionCuentaService = gestionCuentaService;
    }

    @Override
    public List<UsuarioResponseList> findAll() {
        List<Usuario> usuarios = usuarioRepository.findByRoles_Name("ROLE_USER");

        return usuarios.stream()
                .map(usuario -> {
                    UsuarioResponseList dto = new UsuarioResponseList();
                    dto.setUsername(usuario.getUsername());

                    PersonaResponse personaResponse = apiServicePersona
                            .getPersonaInfo(Integer.parseInt(usuario.getUsername()));
                    if (personaResponse != null) {
                        dto.setNombre(personaResponse.getNombreCompleto());
                        dto.setRut(personaResponse.getRut());
                        dto.setVrut(personaResponse.getVrut());
                    }

                    UsuarioDepartamentos usuarioDepartamentos = usuarioDepartamentosService.findByUsuario(usuario);
                    if (usuarioDepartamentos != null) {
                        dto.setIdDepto(usuarioDepartamentos.getIdDepartamento());
                        DepartamentoResponse departamentoResponse = apiDepartamentoService
                                .getDepartamentoById(usuarioDepartamentos.getDepartamento().getId());
                        if (departamentoResponse != null) {
                            dto.setNombreDepartamento(departamentoResponse.getNombre());
                        }
                    }

                    return dto;
                }).toList();
    }

    @Override
    public UsuarioResponse createUser(Usuario usuario) {
        return registroUsuarioService.registrarUsuario(usuario);
    }

    @Override
    public void changeMail(ChangeMailRequest request) {
        gestionCuentaService.solicitarCambioEmail(request);
    }

    @Override
    public UsuarioResponse createUserFunc(UsuarioRequest usuarioRequest) {
        return registroUsuarioService.registrarUsuarioFunc(usuarioRequest);
    }

    @Override
    public UsuarioResponse getUsuario(String username) {
        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format(USER_NOT_FOUND, username));
        UsuarioDepartamentos usuarioDepartamentos = usuarioDepartamentosService.findByUsuario(usuario);
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setUsername(usuario.getUsername());
        usuarioResponse.setIdDepartamento(usuarioDepartamentos.getDepartamento().getId());
        return usuarioResponse;
    }

    @Override
    public Usuario getUsuarioByPersona(Persona persona) {
        return RepositoryUtils.findOrThrow(usuarioRepository.findByPersona(persona),
                String.format("Persona con el rut %d no encontrada", persona.getRut()));
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario findByUsername(String username) {
        return RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format("Usuairo %s no encontrado", username));
    }

    @Override
    public void asignarPerfilesAUsuario(String username, java.util.List<Long> perfilIds) {
        usuarioPerfilService.asignarPerfilesAUsuario(username, perfilIds);
    }

    @Override
    public void agregarPerfilAUsuario(String username, Long perfilId) {
        usuarioPerfilService.agregarPerfilAUsuario(username, perfilId);
    }

    @Override
    public void removerPerfilDeUsuario(String username, Long perfilId) {
        usuarioPerfilService.removerPerfilDeUsuario(username, perfilId);
    }
}
