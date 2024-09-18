package com.java.codigo.examen_spring.controller.advice;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import com.java.codigo.examen_spring.aggregates.response.DataResponse;
import com.java.codigo.examen_spring.controller.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse> exception(Exception exception) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(Constants.COD_ERROR_NOTFOUND);
        dataResponse.setMessage(Constants.MESS_ERROR_NOTFOUND);
        dataResponse.setData(Optional.empty());
        return new ResponseEntity<>(dataResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DataResponse> noFoundException(ResourceNotFoundException resourceNotFoundException) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(Constants.COD_ERROR_NOTFOUND);
        dataResponse.setMessage(Constants.MESS_ERROR_NOTFOUND);
        dataResponse.setData(Optional.empty());
        return new ResponseEntity<>(dataResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DataResponse> checkIllegalArgument(IllegalArgumentException illegalArgumentException) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(Constants.COD_ERROR_ILLEGAL_ARGUMENT);
        dataResponse.setMessage(Constants.MESS_ERROR_ILLEGAL_ARGUMENT);
        dataResponse.setData(Optional.empty());
        return new ResponseEntity<>(dataResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DataResponse> checkAuthenticationException(AuthenticationException authenticationException) {
        DataResponse dataResponse = new DataResponse();
        dataResponse.setCode(Constants.COD_ERROR_EXCEP_AUTH);
        dataResponse.setMessage(Constants.MESS_ERROR_EXCEP_AUTH);
        dataResponse.setData(Optional.empty());
        return new ResponseEntity<>(dataResponse, HttpStatus.NOT_FOUND);
    }
}
