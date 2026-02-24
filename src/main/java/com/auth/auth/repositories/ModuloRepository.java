package com.auth.auth.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Modulo;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    List<Modulo> findBySistemaId(Long idSistema);

}
