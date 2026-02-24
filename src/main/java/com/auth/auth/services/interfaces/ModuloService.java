package com.auth.auth.services.interfaces;

import java.util.List;

import com.auth.auth.dto.ModuloRecordDto;
import com.auth.auth.dto.ModuloRequest;
import com.auth.auth.dto.ModuloResponse;

public interface ModuloService {

    ModuloResponse crearModulo(ModuloRequest request);

    List<ModuloRecordDto> obtenerModulosPorSistema(Long idSistema);

}
