package com.auth.auth.mappers;

import java.util.List;

import com.auth.auth.dto.PerfilRecordResponse;
import com.auth.auth.entities.Perfil;

public interface PerfilRecordMapper {

    PerfilRecordResponse toRecord(Perfil perfil);

    List<PerfilRecordResponse> toRecordList(List<Perfil> perfiles);


}
