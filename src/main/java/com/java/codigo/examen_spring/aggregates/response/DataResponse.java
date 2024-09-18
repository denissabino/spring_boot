package com.java.codigo.examen_spring.aggregates.response;

import com.java.codigo.examen_spring.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {
    private Integer code;
    private String message;
    private Optional data;
}
