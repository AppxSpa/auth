package com.auth.auth.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.auth.auth.dto.PerfilDto;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Permiso;
import com.auth.auth.entities.Perfil;

class PerfilMapperTest {

    @Test
    void mapPerfil_conModulosPermisos() {
        Modulo modulo = new Modulo();
        modulo.setId(5L);
        modulo.setNombre("Modulo 1");

        Permiso permiso = new Permiso();
        permiso.setId(7L);
        permiso.setNombre("PERM_A");

        modulo.setPermisos(new HashSet<>());
        modulo.getPermisos().add(permiso);

        Perfil perfil = new Perfil();
        perfil.setId(3L);
        perfil.setNombre("Perfil X");
        
        perfil.setModulos(new HashSet<>());
        perfil.getModulos().add(modulo);

        PerfilDto dto = PerfilMapper.toDto(perfil);

        assertEquals(3L, dto.getId());
        assertEquals("Perfil X", dto.getNombre());
        assertNotNull(dto.getModulos());
        assertFalse(dto.getModulos().isEmpty());
    }
}
