package com.java.codigo.examen_spring.aggregates.constants;

public class Constants {
    public static final String USER_CREATE = "DSABINO";
    //VARIABLES DE OPERACION
    public static final Integer COD_OK_USER = 2000;
    public static final String MESS_OK_USER = "Operación satisfactoria";
    public static final Integer COD_ERROR_ILLEGAL_ARGUMENT = 4000;
    public static final String MESS_ERROR_ILLEGAL_ARGUMENT = "Los datos proporcionados no son válidos. Por favor, verifica la información enviada.";
    public static final Integer COD_ERROR_EXCEP_AUTH = 4001;
    public static final String MESS_ERROR_EXCEP_AUTH = "Autenticación fallida. Las credenciales no son correctas.";
    public static final Integer COD_ERROR_NOTFOUND = 4004;
    public static final String MESS_ERROR_NOTFOUND = "El recurso solicitado no fue encontrado.";


    //VARIABLES DE REDIS
    public static final String REDIS_KEY_API = "API:EXAMEN:SPRING:";
    public static final int EXPIRE_TIME = 10;

    //VARIABLES PARA JWT
    public static final String CLAIM_ROLE = "ROL";
    public static final Boolean ESTADO_ACTIVO = true;
    public static final String ENPOINTS_FREE_LOGIN = "/api/v1/users/login";
    public static final String ENPOINTS_FREE_REGISTER = "/api/v1/users/register";
    public static final String ENPOINTS_FIND = "/api/v1/users/list";
    public static final String ENPOINTS_FIND_BY = "/api/v1/users/buscar/**";
    public static final String ENPOINTS_UPDATE = "api/v1/users/update";
    public static final String ENPOINTS_DELETE = "api/v1/users/delete";
    public static final String CLAVE_AccountNonExpired = "isAccountNonExpired";
    public static final String CLAVE_AccountNonLocked = "isAccountNonLocked";
    public static final String CLAVE_CredentialsNonExpired = "isCredentialsNonExpired";
    public static final String CLAVE_Enabled = "isEnabled";
}
