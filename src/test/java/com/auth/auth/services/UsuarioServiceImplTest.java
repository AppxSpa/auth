package com.auth.auth.services;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.services.interfaces.ApiDepartamentoService;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.services.interfaces.GestionCuentaService;
import com.auth.auth.services.interfaces.RegistroUsuarioService;
import com.auth.auth.services.interfaces.UsuarioDepartamentosService;
import com.auth.auth.services.interfaces.UsuarioPerfilService;
import com.auth.auth.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioDepartamentosService usuarioDepartamentosService;
    @Mock
    private ApiDepartamentoService apiDepartamentoService;
    @Mock
    private ApiServicePersona apiServicePersona;
    @Mock
    private UsuarioPerfilService usuarioPerfilService;
    @Mock
    private RegistroUsuarioService registroUsuarioService;
    @Mock
    private GestionCuentaService gestionCuentaService;

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setup() {
        usuarioService = new UsuarioServiceImpl(
                usuarioRepository,
                usuarioDepartamentosService,
                apiDepartamentoService,
                apiServicePersona,
                usuarioPerfilService,
                registroUsuarioService,
                gestionCuentaService);
    }

    @Test
    void asignarPerfilesAUsuario_delegaCorrectamente() {
        String username = "alice";
        List<Long> perfilIds = List.of(1L, 2L);

        usuarioService.asignarPerfilesAUsuario(username, perfilIds);

        verify(usuarioPerfilService).asignarPerfilesAUsuario(username, perfilIds);
    }

    @Test
    void agregarPerfilAUsuario_delegaCorrectamente() {
        String username = "bob";
        Long perfilId = 5L;

        usuarioService.agregarPerfilAUsuario(username, perfilId);

        verify(usuarioPerfilService).agregarPerfilAUsuario(username, perfilId);
    }

    @Test
    void removerPerfilDeUsuario_delegaCorrectamente() {
        String username = "carlos";
        Long perfilId = 7L;

        usuarioService.removerPerfilDeUsuario(username, perfilId);

        verify(usuarioPerfilService).removerPerfilDeUsuario(username, perfilId);
    }

    @Test
    void changeMail_delegaCorrectamente() {
        ChangeMailRequest request = new ChangeMailRequest();
        request.setRut(12345678);
        request.setEmail("new.email@example.com");

        usuarioService.changeMail(request);

        verify(gestionCuentaService).solicitarCambioEmail(request);
    }
}
