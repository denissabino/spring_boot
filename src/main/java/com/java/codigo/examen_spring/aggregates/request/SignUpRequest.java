package com.java.codigo.examen_spring.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String numeroDoc;
    private String email;
    private String password;
    private String rol;
}
