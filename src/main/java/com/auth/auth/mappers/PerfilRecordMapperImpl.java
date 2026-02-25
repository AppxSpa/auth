package com.auth.auth.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.auth.auth.dto.ModuloDto;
import com.auth.auth.dto.PerfilRecordResponse;
import com.auth.auth.dto.PermisoDto;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Perfil;

@Component
public class PerfilRecordMapperImpl implements PerfilRecordMapper {

    @Override
    public PerfilRecordResponse toRecord(Perfil perfil) {
        if (perfil == null) {
            return null;
        }

        Long idSistema = null;
        if (perfil.getModulos() != null) {
            idSistema = perfil.getModulos().stream()
                    .filter(m -> m.getSistema() != null)
                    .map(m -> m.getSistema().getId())
                    .findFirst()
                    .orElse(null);
        }

        return new PerfilRecordResponse(
                perfil.getId(),
                perfil.getNombre(),
                idSistema,
                toModuloDtoList(perfil.getModulos()));

    }

    @Override
    public List<PerfilRecordResponse> toRecordList(List<Perfil> perfiles) {
        if (perfiles == null) {
            return List.of();
        }

        return perfiles.stream()
                .map(this::toRecord)
                .toList();
    }

    private List<ModuloDto> toModuloDtoList(Set<Modulo> modulos) {
        if (modulos == null) {
            return List.of();
        }

        return modulos.stream()
                .map(modulo -> new ModuloDto(
                        modulo.getId(),
                        modulo.getNombre(),
                        modulo.getPermisos().stream()
                                .map(permiso -> new PermisoDto(
                                        permiso.getId(),
                                        permiso.getNombre()))
                                .collect(Collectors.toSet())))
                .toList();
    }

}
