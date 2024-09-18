package com.java.codigo.examen_spring.controller;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import com.java.codigo.examen_spring.aggregates.request.SignInRequest;
import com.java.codigo.examen_spring.aggregates.request.SignUpRequest;
import com.java.codigo.examen_spring.aggregates.request.UserRequest;
import com.java.codigo.examen_spring.aggregates.response.DataResponse;
import com.java.codigo.examen_spring.aggregates.response.SignInResponse;
import com.java.codigo.examen_spring.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class userController {

    private final AuthenticationService authenticationService;

    public userController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> singIn(@RequestBody SignInRequest signInRequest) throws Exception {
        System.out.println("prueba de login1");
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }


    @PostMapping("/register")
    public ResponseEntity<DataResponse> createUser(@RequestBody SignUpRequest signUpRequest) throws Exception {
        DataResponse dataResponse = new DataResponse();
        if (signUpRequest.getRol().equals("ADMIN")) {
            dataResponse = authenticationService.sigUpAdmin(signUpRequest);
        } else if (signUpRequest.getRol().equals("USER")) {
            dataResponse = authenticationService.sigUpUser(signUpRequest);
        }

        if (dataResponse.getCode().equals(Constants.COD_OK_USER)) {
            return new ResponseEntity<>(dataResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(dataResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<DataResponse> listUser() throws Exception {
        DataResponse dataResponse = authenticationService.listUsers();
        if (!dataResponse.getCode().equals(Constants.COD_ERROR_NOTFOUND)) {
            return new ResponseEntity<>(dataResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dataResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/find/{dni}")
    public ResponseEntity<DataResponse> findUser(@PathVariable("dni") String dni) throws Exception {
        DataResponse dataResponse = authenticationService.findUser(dni);
        if (!dataResponse.getCode().equals(Constants.COD_ERROR_NOTFOUND)) {
            return new ResponseEntity<>(dataResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dataResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataResponse> updateUser(@PathVariable("id") Long id,
                                                   @RequestBody UserRequest userRequest) throws Exception {
        DataResponse dataResponse = authenticationService.updateUser(id, userRequest);
        if (!dataResponse.getCode().equals(Constants.COD_ERROR_NOTFOUND)) {
            return new ResponseEntity<>(dataResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dataResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DataResponse> deleteUser(@PathVariable("id") Long id) throws Exception {
        DataResponse dataResponse = authenticationService.deleteUser(id);
        if (!dataResponse.getCode().equals(Constants.COD_ERROR_NOTFOUND)) {
            return new ResponseEntity<>(dataResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dataResponse, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
