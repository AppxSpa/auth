package com.auth.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.auth.auth.entities.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
}