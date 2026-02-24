package com.auth.auth.mappers;

import java.util.List;

import com.auth.auth.dto.ModuloRecordDto;
import com.auth.auth.entities.Modulo;

public interface ModuloRecordMapper {

    List<ModuloRecordDto> toRecordList(List<Modulo> modulos);

}
