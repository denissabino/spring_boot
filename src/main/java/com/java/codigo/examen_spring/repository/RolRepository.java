package com.java.codigo.examen_spring.repository;

import com.java.codigo.examen_spring.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<RolEntity, Long> {
    Optional<RolEntity> findByNombreRol(String nombreRol);
}
