package com.auth.auth.services.interfaces;

import com.auth.auth.dto.ChangeMailRequest;

public interface GestionCuentaService {
    void solicitarCambioEmail(ChangeMailRequest request);
}
