package com.auth.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    // otros deps del constructor no son necesarios para estas pruebas
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Mock
    private com.auth.auth.configuration.ApiProperties apiProperties;
    @Mock
    private com.auth.auth.services.interfaces.ApiServicePersona apiServicePersona;
    @Mock
    private com.auth.auth.services.interfaces.ApiServiceMail apiServiceMail;
    @Mock
    private com.auth.auth.services.interfaces.UsuarioDepartamentosService usuarioDepartamentosService;
    @Mock
    private com.auth.auth.services.interfaces.DepartamentoService departamentoService;
    @Mock
    private com.auth.auth.services.interfaces.RolService rolService;
    @Mock
    private com.auth.auth.services.interfaces.PersonaService personaService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setup() {
        // nothing special
    }

    @Test
    void asignarPerfilesAUsuario_reemplazaPerfilesExistentes() {
        Usuario usuario = new Usuario();
        usuario.setUsername("alice");
        usuario.setPerfiles(new ArrayList<>());

        when(usuarioRepository.findByUsername("alice")).thenReturn(Optional.of(usuario));

        Perfil p1 = new Perfil(); p1.setId(1L);
        Perfil p2 = new Perfil(); p2.setId(2L);
        List<Perfil> perfiles = List.of(p1, p2);

        when(perfilRepository.findAllById(List.of(1L, 2L))).thenReturn(perfiles);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.asignarPerfilesAUsuario("alice", List.of(1L, 2L));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario saved = captor.getValue();
        assertEquals(2, saved.getPerfiles().size());
    }

    @Test
    void agregarPerfilAUsuario_agregaCuandoNoExiste() {
        Usuario usuario = new Usuario();
        usuario.setUsername("bob");
        usuario.setPerfiles(new ArrayList<>());

        when(usuarioRepository.findByUsername("bob")).thenReturn(Optional.of(usuario));

        Perfil nuevo = new Perfil(); nuevo.setId(5L);
        when(perfilRepository.findById(5L)).thenReturn(Optional.of(nuevo));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.agregarPerfilAUsuario("bob", 5L);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario saved = captor.getValue();
        assertEquals(1, saved.getPerfiles().size());
        assertEquals(5L, saved.getPerfiles().get(0).getId());
    }

    @Test
    void removerPerfilDeUsuario_remueveSiExiste() {
        Usuario usuario = new Usuario();
        usuario.setUsername("carlos");
        Perfil existing = new Perfil(); existing.setId(7L);
        List<Perfil> list = new ArrayList<>();
        list.add(existing);
        usuario.setPerfiles(list);

        when(usuarioRepository.findByUsername("carlos")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.removerPerfilDeUsuario("carlos", 7L);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario saved = captor.getValue();
        assertEquals(0, saved.getPerfiles().size());
    }
}
