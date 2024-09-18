package com.java.codigo.examen_spring.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.codigo.examen_spring.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UtilConver {
    private ObjectMapper objectMapper;

    public String converToString(UserEntity userEntity) {
        try {
            objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T converFromString(String json, Class<T> claseConvertida) {
        try {
            objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, claseConvertida);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
