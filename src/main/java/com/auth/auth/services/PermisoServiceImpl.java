package com.auth.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.auth.dto.PermisoRequest;
import com.auth.auth.dto.PermisoResponse;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Permiso;
import com.auth.auth.repositories.ModuloRepository;
import com.auth.auth.repositories.PermisoRepository;
import com.auth.auth.services.interfaces.PermisoService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class PermisoServiceImpl implements PermisoService {

    private final PermisoRepository permisoRepository;
    private final ModuloRepository moduloRepository;

    public PermisoServiceImpl(PermisoRepository permisoRepository, ModuloRepository moduloRepository) {
        this.permisoRepository = permisoRepository;
        this.moduloRepository = moduloRepository;
    }

    @Override
    @Transactional
    public PermisoResponse crearPermiso(PermisoRequest request) {
        // Relacionar Permiso con Módulo
        Modulo modulo = RepositoryUtils.findOrThrow(moduloRepository.findById(request.getModuloId()),
                "Módulo no encontrado con id " + request.getModuloId());

        Permiso permiso = new Permiso();
        permiso.setNombre(request.getNombre());
        permiso.setModulo(modulo);

        Permiso guardado = permisoRepository.save(permiso);

        return new PermisoResponse(guardado.getId(), guardado.getNombre(), modulo.getNombre());
    }
}