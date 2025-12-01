package com.auth.auth.utils;

import com.auth.auth.dto.ModuloDto;
import com.auth.auth.dto.PerfilDto;
import com.auth.auth.dto.PermisoDto;
import com.auth.auth.dto.SistemaDto;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;

public class PerfilMapper {

    private PerfilMapper() {
        // Util class
    }

    public static PerfilDto toDto(Perfil perfil) {
        PerfilDto dto = new PerfilDto();
        dto.setId(perfil.getId());
        dto.setNombre(perfil.getNombre());

        Sistema sistema = perfil.getSistema();
        if (sistema != null) {
            // construir SistemaDto (sin tomar modulos desde sistema)
            SistemaDto sistemaDto = new SistemaDto();
            sistemaDto.setNombreSistema(sistema.getNombre());
            sistemaDto.setCodSistema(sistema.getCodigo());
            dto.setSistema(sistemaDto);
        }

        // mapear modulos desde el propio perfil
        if (perfil.getModulos() != null) {
            java.util.Set<ModuloDto> modulosDto = perfil.getModulos().stream().map(modulo -> {
                ModuloDto moduloDto = new ModuloDto();
                moduloDto.setIdModulo(modulo.getId());
                moduloDto.setNombreModulo(modulo.getNombre());

                java.util.Set<PermisoDto> permisoDtos = modulo.getPermisos().stream()
                        .map(permiso -> new PermisoDto(permiso.getId(), permiso.getNombre()))
                        .collect(java.util.stream.Collectors.toSet());

                moduloDto.setPermisos(permisoDtos);
                return moduloDto;
            }).collect(java.util.stream.Collectors.toSet());

            dto.setModulos(modulosDto);
        }

        return dto;
    }

    public static SistemaDto sistemaToDto(Sistema sistema) {
        SistemaDto sistemaDto = new SistemaDto();
        sistemaDto.setNombreSistema(sistema.getNombre());
        sistemaDto.setCodSistema(sistema.getCodigo());
        // Nota: este método sigue disponible si se necesita mapear todos los módulos de un sistema
        java.util.Set<ModuloDto> modulosDto = sistema.getModulos().stream().map(modulo -> {
            ModuloDto moduloDto = new ModuloDto();
            moduloDto.setIdModulo(modulo.getId());
            moduloDto.setNombreModulo(modulo.getNombre());

            java.util.Set<PermisoDto> permisoDtos = modulo.getPermisos().stream()
                    .map(permiso -> new PermisoDto(permiso.getId(), permiso.getNombre()))
                    .collect(java.util.stream.Collectors.toSet());

            moduloDto.setPermisos(permisoDtos);
            return moduloDto;
        }).collect(java.util.stream.Collectors.toSet());

        sistemaDto.setModulos(modulosDto);
        return sistemaDto;
    }
}
