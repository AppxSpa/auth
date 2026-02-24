package com.auth.auth.services.interfaces;

import com.auth.auth.dto.PerfilRecordResponse;
import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;
import java.util.List;

public interface PerfilService {

       PerfilResponse crearPerfil(PerfilRequest request);

       List<PerfilRecordResponse> obtenerPerfiles();

}
