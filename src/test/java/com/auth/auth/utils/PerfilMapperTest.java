package com.auth.auth.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.auth.auth.dto.PerfilDto;
import com.auth.auth.dto.SistemaDto;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Permiso;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;

class PerfilMapperTest {

    @Test
    void mapPerfil_conSistema_yModulosPermisos() {
        Sistema sistema = new Sistema();
        sistema.setId(2L);
        sistema.setNombre("Sistema X");
        sistema.setCodigo("SX");

        Modulo modulo = new Modulo();
        modulo.setId(5L);
        modulo.setNombre("Modulo 1");

        Permiso permiso = new Permiso();
        permiso.setId(7L);
        permiso.setNombre("PERM_A");

        modulo.setPermisos(new HashSet<>());
        modulo.getPermisos().add(permiso);

        sistema.setModulos(new HashSet<>());
        sistema.getModulos().add(modulo);

        Perfil perfil = new Perfil();
        perfil.setId(3L);
        perfil.setNombre("Perfil X");
        perfil.setSistema(sistema);

        PerfilDto dto = PerfilMapper.toDto(perfil);

        assertEquals(3L, dto.getId());
        assertEquals("Perfil X", dto.getNombre());
        SistemaDto sDto = dto.getSistema();
        assertEquals("Sistema X", sDto.getNombreSistema());
        assertEquals("SX", sDto.getCodSistema());
    }
}
