package com.java.codigo.examen_spring.service;

import com.java.codigo.examen_spring.aggregates.request.SignInRequest;
import com.java.codigo.examen_spring.aggregates.request.SignUpRequest;
import com.java.codigo.examen_spring.aggregates.request.UserRequest;
import com.java.codigo.examen_spring.aggregates.response.DataResponse;
import com.java.codigo.examen_spring.aggregates.response.SignInResponse;

public interface AuthenticationService {
    //iniciar sesión
    SignInResponse signIn(SignInRequest signInRequest) throws Exception;

    //inscribirse
    DataResponse sigUpUser(SignUpRequest signUpRequest) throws Exception;

    DataResponse sigUpAdmin(SignUpRequest signUpRequest) throws Exception;

    //trasacciones
    DataResponse listUsers() throws Exception;

    DataResponse findUser(String dni) throws Exception;

    DataResponse updateUser(Long id, UserRequest userRequest) throws Exception;

    DataResponse deleteUser(Long dni) throws Exception;

}
