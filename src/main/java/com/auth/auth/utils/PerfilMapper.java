package com.auth.auth.utils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.auth.auth.dto.ModuloDto;
import com.auth.auth.dto.PerfilDto;
import com.auth.auth.dto.PermisoDto;
import com.auth.auth.dto.SistemaDto;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;

public final class PerfilMapper {

    private PerfilMapper() {
        // Utility class
    }

    public static PerfilDto toDto(Perfil perfil) {
        PerfilDto dto = new PerfilDto();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());

        // mapear modulos desde el propio perfil
        dto.setModulos(
                Optional.ofNullable(perfil.getModulos())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(PerfilMapper::toModuloDto)
                        .collect(Collectors.toSet()));

        return dto;
    }

    public static SistemaDto sistemaToDto(Sistema sistema) {
        SistemaDto sistemaDto = new SistemaDto();
        sistemaDto.setNombreSistema(sistema.getNombre());
        sistemaDto.setCodSistema(sistema.getCodigo());

        // Nota: este método sigue disponible si se necesita mapear todos los módulos de un sistema
        sistemaDto.setModulos(
                Optional.ofNullable(sistema.getModulos())
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(PerfilMapper::toModuloDto)
                        .collect(Collectors.toSet()));

        return sistemaDto;
    }

    private static ModuloDto toModuloDto(Modulo modulo) {
        ModuloDto moduloDto = new ModuloDto();
        moduloDto.setIdModulo(modulo.getId());
        moduloDto.setNombreModulo(modulo.getNombre());

        Set<PermisoDto> permisoDtos = Optional.ofNullable(modulo.getPermisos())
                .orElse(Collections.emptySet())
                .stream()
                .map(permiso -> new PermisoDto(permiso.getId(), permiso.getNombre()))
                .collect(Collectors.toSet());

        moduloDto.setPermisos(permisoDtos);
        return moduloDto;
    }
}
