package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;
import com.auth.auth.entities.Modulo;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.repositories.ModuloRepository;
import com.auth.auth.services.interfaces.PerfilService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;

    private final SistemaRepository sistemaRepository;
    private final ModuloRepository moduloRepository;

    public PerfilServiceImpl(PerfilRepository perfilRepository, SistemaRepository sistemaRepository,
            ModuloRepository moduloRepository) {
        this.perfilRepository = perfilRepository;
        this.sistemaRepository = sistemaRepository;
        this.moduloRepository = moduloRepository;
    }

    private static final String MODULE_NOT_FOUND = "Modulo con %d no encontrado";
    private static final String SISTEMA_NOT_FOUND = "Sistema con %d no encontrado";

    @Override
    public PerfilResponse crearPerfil(PerfilRequest request) {
        Perfil perfil = new Perfil();
        perfil.setNombre(request.getNombre());

        Sistema sistema = getSitemaById(request.getSistemaId());
        perfil.setSistema(sistema);

        // Si se enviaron moduloIds, resolver y asignar módulos (validando que pertenezcan al sistema)
        if (request.getModuloIds() != null && !request.getModuloIds().isEmpty()) {
            java.util.Set<Modulo> modulos = new java.util.HashSet<>();
            for (Long moduloId : request.getModuloIds()) {
                Modulo modulo = RepositoryUtils.findOrThrow(moduloRepository.findById(moduloId),
                    String.format(MODULE_NOT_FOUND, moduloId));
                if (modulo.getSistema() == null || !modulo.getSistema().getId().equals(sistema.getId())) {
                    throw new IllegalArgumentException(String.format(
                            "El modulo %d no pertenece al sistema %d esperado", moduloId, sistema.getId()));
                }
                modulos.add(modulo);
            }
            perfil.setModulos(modulos);
        }

        Perfil guardado = perfilRepository.save(perfil);

        String nombreSistema = guardado.getSistema() != null ? guardado.getSistema().getNombre() : null;

        PerfilResponse resp = new PerfilResponse(guardado.getId(), guardado.getNombre(), nombreSistema);
        // agregar nombres de modulos al response
        if (guardado.getModulos() != null && !guardado.getModulos().isEmpty()) {
            java.util.Set<String> nombres = new java.util.HashSet<>();
            for (Modulo m : guardado.getModulos()) {
                nombres.add(m.getNombre());
            }
            resp.setModulos(nombres);
        }

        return resp;
    }

    private Sistema getSitemaById(Long idSistema) {
        return RepositoryUtils.findOrThrow(sistemaRepository.findById(idSistema),
            String.format(SISTEMA_NOT_FOUND, idSistema));
    }
}