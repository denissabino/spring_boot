package com.java.codigo.examen_spring.dto;

import com.java.codigo.examen_spring.entity.RolEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String tipoDoc;
    private String numeroDoc;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private Set<RolEntity> roles;
}
