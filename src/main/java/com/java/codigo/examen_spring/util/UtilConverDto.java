package com.java.codigo.examen_spring.util;

import com.java.codigo.examen_spring.dto.UserDto;
import com.java.codigo.examen_spring.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UtilConverDto {
    public UserDto getDataresponse(UserEntity userEntity) {
        UserDto response = new UserDto();
        response.setTipoDoc(userEntity.getTipoDoc());
        response.setNumeroDoc(userEntity.getNumeroDoc());
        response.setId(userEntity.getId());
        response.setNombres(userEntity.getNombres());
        response.setApellidoPaterno(userEntity.getApellidoPaterno());
        response.setApellidoMaterno(userEntity.getApellidoMaterno());
        response.setEmail(userEntity.getEmail());
        response.setRoles(userEntity.getRoles());
        return response;
    }
}
