package com.auth.auth.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.SistemaRepository;

@ExtendWith(MockitoExtension.class)
class PerfilServiceImplTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private SistemaRepository sistemaRepository;

    @InjectMocks
    private PerfilServiceImpl perfilService;

    private Sistema sistema;

    @BeforeEach
    void setup() {
        sistema = new Sistema();
        sistema.setId(1L);
        sistema.setNombre("Sistema A");
        sistema.setCodigo("SYS_A");
    }

    @Test
    void crearPerfil_asignaSistemaYRetornaNombreSistema() {
        PerfilRequest req = new PerfilRequest();
        req.setNombre("Perfil Test");
        req.setSistemaId(1L);

        when(sistemaRepository.findById(1L)).thenReturn(Optional.of(sistema));

        Perfil saved = new Perfil();
        saved.setId(10L);
        saved.setNombre(req.getNombre());
        saved.setSistema(sistema);

        when(perfilRepository.save(any(Perfil.class))).thenReturn(saved);

        PerfilResponse resp = perfilService.crearPerfil(req);

        assertEquals(10L, resp.getId());
        assertEquals("Perfil Test", resp.getNombre());
        assertEquals("Sistema A", resp.getSistema());
    }
}
