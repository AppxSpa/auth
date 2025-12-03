package com.auth.auth.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Departamento;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.services.interfaces.DepartamentoService;
import com.auth.auth.services.interfaces.PersonaService;
import com.auth.auth.services.interfaces.RegistroUsuarioService;
import com.auth.auth.services.interfaces.UsuarioDepartamentosService;
import com.auth.auth.services.interfaces.UsuarioRolService;

@Service
public class RegistroUsuarioServiceImpl implements RegistroUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRolService usuarioRolService;
    private final PersonaService personaService;
    private final ApiServicePersona apiServicePersona;
    private final ApiServiceMail apiServiceMail;
    private final ApiProperties apiProperties;
    private final DepartamentoService departamentoService;
    private final UsuarioDepartamentosService usuarioDepartamentosService;

    public RegistroUsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            UsuarioRolService usuarioRolService, PersonaService personaService, ApiServicePersona apiServicePersona,
            ApiServiceMail apiServiceMail, ApiProperties apiProperties, DepartamentoService departamentoService,
            UsuarioDepartamentosService usuarioDepartamentosService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRolService = usuarioRolService;
        this.personaService = personaService;
        this.apiServicePersona = apiServicePersona;
        this.apiServiceMail = apiServiceMail;
        this.apiProperties = apiProperties;
        this.departamentoService = departamentoService;
        this.usuarioDepartamentosService = usuarioDepartamentosService;
    }

    @Override
    public UsuarioResponse registrarUsuario(Usuario usuario) {
        usuario.setRoles(usuarioRolService.getRolesParaUsuario(usuario.isAdmin(), usuario.isFunc()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivationToken(usuario.generateActivationToken());

        int rut = Integer.parseInt(usuario.getUsername());
        Persona persona = personaService.getPersonaByRut(rut);
        PersonaResponse personaResponse = apiServicePersona.getPersonaInfo(persona.getRut());
        usuario.setPersona(persona);

        sendMailActivation(usuario, personaResponse);

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getUsername(), usuario.getActivationToken());
    }

    @Override
    public UsuarioResponse registrarUsuarioFunc(UsuarioRequest usuarioRequest) {
        List<Rol> roles = usuarioRolService.getRolesParaUsuario(usuarioRequest.isAdmin(), usuarioRequest.isFunc());
        Departamento depto = departamentoService.getById(usuarioRequest.getIdDepto());

        return usuarioRepository.findByUsername(usuarioRequest.getRut().toString())
                .map(usuarioExistente -> {
                    usuarioRolService.actualizarRoles(usuarioExistente, roles);
                    return new UsuarioResponse(usuarioExistente.getUsername());
                })
                .orElseGet(() -> {
                    Persona persona = getOrCreatePersonaFromApi(usuarioRequest);

                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setUsername(usuarioRequest.getRut().toString());
                    nuevoUsuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
                    nuevoUsuario.setRoles(roles);
                    nuevoUsuario.setPersona(persona);
                    nuevoUsuario.setEnabled(true);

                    nuevoUsuario = usuarioRepository.save(nuevoUsuario);

                    UsuarioDepartamentos usuarioDepartamentos = new UsuarioDepartamentos(nuevoUsuario, depto);
                    usuarioDepartamentosService.save(usuarioDepartamentos);

                    return new UsuarioResponse(nuevoUsuario.getUsername());
                });
    }

    private void sendMailActivation(Usuario usuario, PersonaResponse personaResponse) {
        String activationLink = apiProperties.getActivationUrl() + usuario.getActivationToken();
        Map<String, Object> variables = Map.of("nombre", personaResponse.getNombres(), "link", activationLink);

        try {
            apiServiceMail.sendEmail(personaResponse.getEmail(), "Activa tu registro", "register-template", variables);
        } catch (SendMailExceptions e) {
            throw new SendMailExceptions("Error enviando correo de activación a " + personaResponse.getEmail());
        }
    }

    private Persona getOrCreatePersonaFromApi(UsuarioRequest usuarioRequest) {
        PersonaResponse personaResponse = apiServicePersona.getPersonaInfo(usuarioRequest.getRut());
        Optional<PersonaResponse> optionalPersonaResponse = Optional.ofNullable(personaResponse);
        Persona persona = personaService.getPersonaByRut(usuarioRequest.getRut());

        if (optionalPersonaResponse.isEmpty() && persona == null) {
            PersonaRequest newPersonaRequest = new PersonaRequest();
            newPersonaRequest.setRut(usuarioRequest.getRut());
            newPersonaRequest.setVrut(usuarioRequest.getVrut());
            newPersonaRequest.setNombres(usuarioRequest.getNombres());
            newPersonaRequest.setPaterno(usuarioRequest.getPaterno());
            newPersonaRequest.setMaterno(usuarioRequest.getMaterno());
            newPersonaRequest.setEmail(usuarioRequest.getEmail());

            apiServicePersona.createPersona(newPersonaRequest);

            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaService.save(persona);
        }

        if (optionalPersonaResponse.isPresent() && persona == null) {
            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaService.save(persona);
        }

        return persona;
    }
}
