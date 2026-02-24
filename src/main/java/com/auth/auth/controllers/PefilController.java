package com.auth.auth.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.ApiError;
import com.auth.auth.dto.PerfilRecordResponse;
import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;
import com.auth.auth.services.interfaces.PerfilService;

@RestController
@RequestMapping("/api/auth/usuarios/perfiles")
public class PefilController {

    private final PerfilService perfilService;

    public PefilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping
    public ResponseEntity<Object> crearPerfil(@RequestBody PerfilRequest request) {
        try {
            PerfilResponse perfil = perfilService.crearPerfil(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Error interno", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> obtenerPerfiles() {

        List<PerfilRecordResponse> perfiles = perfilService.obtenerPerfiles();
        if (perfiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "No se encontraron perfiles disponibles"));
        }

        return ResponseEntity.ok(perfiles);

    }

}