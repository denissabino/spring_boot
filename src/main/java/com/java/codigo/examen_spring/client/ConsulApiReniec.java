package com.java.codigo.examen_spring.client;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import com.java.codigo.examen_spring.aggregates.request.SignUpRequest;
import com.java.codigo.examen_spring.aggregates.response.ReniecResponse;
import com.java.codigo.examen_spring.entity.RolEntity;
import com.java.codigo.examen_spring.entity.UserEntity;
import com.java.codigo.examen_spring.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Objects;

@Component
public class ConsulApiReniec {
    @Autowired
    private ReniecCliente reniecCliente;
    @Autowired
    private RolRepository rolRepository;
    @Value("${token.api}")
    private String tokenApi;

    public UserEntity consultDataReniec(SignUpRequest signUpRequest) {
        UserEntity userEntity = new UserEntity();
        //consultamos la api para datos reniec
        String auth = "Bearer " + tokenApi;
        ReniecResponse reniecResponse = reniecCliente.getPersonReniec(signUpRequest.getNumeroDoc(), auth);
        if (Objects.nonNull(reniecResponse)) {
            userEntity.setTipoDoc(reniecResponse.getTipoDocumento());
            userEntity.setNumeroDoc(reniecResponse.getNumeroDocumento());
            userEntity.setNombres(reniecResponse.getNombres());
            userEntity.setApellidoPaterno(reniecResponse.getApellidoPaterno());
            userEntity.setApellidoMaterno(reniecResponse.getApellidoMaterno());
            userEntity.setEmail(signUpRequest.getEmail());
            userEntity.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
            userEntity.setRoles(Collections.singleton(getFindRoles(signUpRequest.getRol())));
            userEntity.setIsAccountNonExpired(Constants.ESTADO_ACTIVO);
            userEntity.setIsAccountNonLocked(Constants.ESTADO_ACTIVO);
            userEntity.setIsCredentialsNonExpired(Constants.ESTADO_ACTIVO);
            userEntity.setIsEnabled(Constants.ESTADO_ACTIVO);
            userEntity.setUserCrea(Constants.USER_CREATE);
            userEntity.setFechaCrea(new Timestamp(System.currentTimeMillis()));
            return userEntity;
        }
        return null;
    }
    private RolEntity getFindRoles(String rolUser) {
        return rolRepository.findByNombreRol(rolUser).
                orElseThrow(() -> new RuntimeException("NO EXISTE EL ROL SOLICITADO:" + rolUser));
    }
}
