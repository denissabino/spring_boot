package com.java.codigo.examen_spring.repository;

import com.java.codigo.examen_spring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByNumeroDoc(String numDoc);

    Optional<UserEntity> findByEmail(String email);
}
