package com.auth.auth.services.interfaces;

import com.auth.auth.dto.DepartamentoResponse;

public interface ApiDepartamentoService {

    DepartamentoResponse getDepartamentoById(Long id);

}
