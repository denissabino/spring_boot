package com.java.codigo.examen_spring.service.impl;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import com.java.codigo.examen_spring.aggregates.request.SignInRequest;
import com.java.codigo.examen_spring.aggregates.request.SignUpRequest;
import com.java.codigo.examen_spring.aggregates.request.UserRequest;
import com.java.codigo.examen_spring.aggregates.response.DataResponse;
import com.java.codigo.examen_spring.aggregates.response.SignInResponse;
import com.java.codigo.examen_spring.client.ConsulApiReniec;
import com.java.codigo.examen_spring.controller.exception.ResourceNotFoundException;
import com.java.codigo.examen_spring.dto.UserDto;
import com.java.codigo.examen_spring.entity.UserEntity;
import com.java.codigo.examen_spring.redis.ConsulPersonRedis;
import com.java.codigo.examen_spring.repository.UserRepository;
import com.java.codigo.examen_spring.service.AuthenticationService;
import com.java.codigo.examen_spring.service.JwtService;
import com.java.codigo.examen_spring.util.UtilConverDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final ConsulApiReniec consulApiReniec;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UtilConverDto utilConverDto;
    private final ConsulPersonRedis consulPersonRedis;

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) throws Exception {
        SignInResponse response = new SignInResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(), signInRequest.getPassword()));
        Optional<UserEntity> user = userRepository.findByEmail(signInRequest.getEmail());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("No se enceontró el usuario");
        }
        var token = jwtService.generateToken(user.get());
        response.setToken(token);
        response.setMessage("Logeo satisfactoria");
        return response;
    }

    @Override
    @Transactional
    public DataResponse sigUpUser(SignUpRequest signUpRequest) throws Exception {
        UserEntity dataResponse = consulApiReniec.consultDataReniec(signUpRequest);
        if (Objects.isNull(dataResponse)) {
            throw new ResourceNotFoundException("No se encontró el dni en reniec");
        }
        UserDto responseDto = utilConverDto.getDataresponse(userRepository.save(dataResponse));
        return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER,
                Optional.of(responseDto));
    }

    @Override
    @Transactional
    public DataResponse sigUpAdmin(SignUpRequest signUpRequest) throws Exception {
        UserEntity dataResponse = consulApiReniec.consultDataReniec(signUpRequest);
        if (Objects.isNull(dataResponse)) {
            throw new ResourceNotFoundException("No se encontró el dni en reniec");
        }
        UserDto responseDto = utilConverDto.getDataresponse(userRepository.save(dataResponse));
        return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER,
                Optional.of(responseDto));
    }

    @Override
    public DataResponse listUsers() throws Exception {
        List<UserEntity> listUser = userRepository.findAll();
        List<UserDto> listDto = new ArrayList<>();
        if (!listUser.isEmpty()) {
            for (UserEntity row : listUser) {
                listDto.add(utilConverDto.getDataresponse(row));
            }
            return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER, Optional.of(listDto));
        } else {
            return new DataResponse(Constants.COD_ERROR_NOTFOUND, Constants.MESS_ERROR_NOTFOUND, Optional.empty());
        }
    }

    @Override
    public DataResponse findUser(String dni) throws Exception {
        Optional<UserEntity> userEntity = consulPersonRedis.findPersonRedis(dni);

        if (userEntity.isPresent()) {
            UserDto responseDto = utilConverDto.getDataresponse(userEntity.get());
            return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER, Optional.of(responseDto));
        } else {
            return new DataResponse(Constants.COD_ERROR_NOTFOUND, Constants.MESS_ERROR_NOTFOUND, Optional.empty());
        }
    }

    @Override
    public DataResponse updateUser(Long id, UserRequest userRequest) throws Exception {

        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            userEntity.get().setTipoDoc(userRequest.getTipoDocumento());
            userEntity.get().setNombres(userRequest.getNombres());
            userEntity.get().setApellidoPaterno(userRequest.getApellidoPaterno());
            userEntity.get().setApellidoMaterno(userRequest.getApellidoMaterno());
            userEntity.get().setEmail(userRequest.getEmail());
            userEntity.get().setUserUpdate(Constants.USER_CREATE);
            userEntity.get().setFechaUpdate(new Timestamp(System.currentTimeMillis()));
            UserDto responseDto = utilConverDto.getDataresponse(userRepository.save(userEntity.get()));
            return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER,
                    Optional.of(responseDto));
        } else {
            return new DataResponse(Constants.COD_ERROR_NOTFOUND, Constants.MESS_ERROR_NOTFOUND, Optional.empty());
        }
    }

    @Override
    public DataResponse deleteUser(Long id) throws Exception {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            userEntity.get().setUserUpdate(Constants.USER_CREATE);
            userEntity.get().setFechaUpdate(new Timestamp(System.currentTimeMillis()));
            //eliminando de redis si existiera
            consulPersonRedis.deletePersonaRedis(userEntity.get().getNumeroDoc());
            //eliminando
            userRepository.delete(userEntity.get());
            UserDto responseDto = utilConverDto.getDataresponse(userEntity.get());
            return new DataResponse(Constants.COD_OK_USER, Constants.MESS_OK_USER,
                    Optional.of(responseDto));
        } else {
            return new DataResponse(Constants.COD_ERROR_NOTFOUND, Constants.MESS_ERROR_NOTFOUND, Optional.empty());
        }
    }
}
