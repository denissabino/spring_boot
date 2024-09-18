package com.java.codigo.examen_spring.aggregates.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponse {
    private String token;
    private String message;
}
