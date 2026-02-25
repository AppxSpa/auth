package com.auth.auth.dto;

import java.util.List;

public record PerfilRecordResponse(Long idPerfil,
        String nombrePerfil,
        Long idSistema,
        List<ModuloDto> modulos) {

    // constructor auxiliar que deja la lista de módulos vacía
    public PerfilRecordResponse(Long idPerfil,
            String nombrePerfil,
            Long idSistema) {
        this(idPerfil, nombrePerfil, idSistema, List.of());
    }
}