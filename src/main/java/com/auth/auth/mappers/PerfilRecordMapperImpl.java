package com.auth.auth.mappers;

import java.util.List;

import com.auth.auth.dto.PerfilRecordResponse;
import com.auth.auth.entities.Perfil;

public class PerfilRecordMapperImpl implements PerfilRecordMapper {

    @Override
    public PerfilRecordResponse toRecord(Perfil perfil) {
        if (perfil == null) {
            return null;
        }

        return new PerfilRecordResponse(
                perfil.getId(),
                perfil.getNombre(),
                perfil.getSistema().getId());

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

    

}
