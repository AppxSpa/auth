package com.auth.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.auth.dto.ModuloRecordDto;
import com.auth.auth.dto.ModuloRequest;
import com.auth.auth.dto.ModuloResponse;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Sistema;
import com.auth.auth.mappers.ModuloRecordMapper;
import com.auth.auth.repositories.ModuloRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.ModuloService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;
    private final SistemaRepository sistemaRepository;
    private final ModuloRecordMapper moduloRecordMapper;

    public ModuloServiceImpl(ModuloRepository moduloRepository, SistemaRepository sistemaRepository,
            ModuloRecordMapper moduloRecordMapper) {
        this.moduloRecordMapper = moduloRecordMapper;
        this.moduloRepository = moduloRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public ModuloResponse crearModulo(ModuloRequest request) {
        Sistema sistema = getSistemaById(request.getSistemaId());

        Modulo modulo = new Modulo();
        modulo.setCodigo(request.getCodigo());
        modulo.setNombre(request.getNombre());
        modulo.setSistema(sistema);

        Modulo guardado = moduloRepository.save(modulo);

        return new ModuloResponse(
                guardado.getId(),
                guardado.getCodigo(),
                guardado.getNombre(),
                guardado.getSistema().getNombre());
    }

    private Sistema getSistemaById(Long idSistema) {
        return RepositoryUtils.findOrThrow(sistemaRepository.findById(idSistema),
                String.format("No se encontró el sistmea con el id %d", idSistema));
    }

    @Override
    public List<ModuloRecordDto> obtenerModulosPorSistema(Long idSistema) {

        List<Modulo> modulos = moduloRepository.findBySistemaId(idSistema);
        return moduloRecordMapper.toRecordList(modulos);
    }
}
