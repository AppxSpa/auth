package com.auth.auth.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.services.interfaces.GestionCuentaService;
import com.auth.auth.services.interfaces.PersonaService;

@Service
public class GestionCuentaServiceImpl implements GestionCuentaService {

    private final PersonaService personaService;
    private final UsuarioRepository usuarioRepository;
    private final ApiProperties apiProperties;
    private final ApiServiceMail apiServiceMail;

    public GestionCuentaServiceImpl(PersonaService personaService, UsuarioRepository usuarioRepository,
            ApiProperties apiProperties, ApiServiceMail apiServiceMail) {
        this.personaService = personaService;
        this.usuarioRepository = usuarioRepository;
        this.apiProperties = apiProperties;
        this.apiServiceMail = apiServiceMail;
    }

    @Override
    public void solicitarCambioEmail(ChangeMailRequest request) {
        Integer rut = request.getRut();
        String email = request.getEmail();

        Persona persona = personaService.getPersonaByRut(rut);
        Usuario usuario = usuarioRepository.findByPersona(persona).orElseThrow();

        usuario.setEnabled(false);
        usuario.setActivationToken(usuario.generateActivationToken());
        usuarioRepository.save(usuario);

        String activationLink = apiProperties.getActivationUrl() + usuario.getActivationToken();
        Map<String, Object> variables = Map.of("nombre", usuario.getUsername(), "codigo", activationLink);

        try {
            apiServiceMail.sendEmail(email, "Correo con Thymeleaf", "email-template", variables);
        } catch (Exception e) {
            // Se recomienda un manejo de excepciones más robusto que solo imprimir el
            // stack trace.
            e.printStackTrace();
        }
    }
}
