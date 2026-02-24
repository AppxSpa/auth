package com.auth.auth.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.auth.auth.dto.ModuloRecordDto;
import com.auth.auth.entities.Modulo;
import java.util.Collections;

@Component
public class ModuloRecordMapperImpl implements ModuloRecordMapper {

    @Override
    public List<ModuloRecordDto> toRecordList(List<Modulo> modulos) {

        if (modulos == null) {
            return Collections.emptyList();
        }

        return modulos.stream()
                .map(modulo -> new ModuloRecordDto(
                        modulo.getId(),
                        modulo.getNombre(),
                        modulo.getSistema().getId()))
                .toList();
       
    }

    

}
