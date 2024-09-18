package com.java.codigo.examen_spring.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extracUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);
}
