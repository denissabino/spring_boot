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
import com.java.codigo.examen_spring.redis.RedisService;
import com.java.codigo.examen_spring.repository.UserRepository;
import com.java.codigo.examen_spring.service.JwtService;
import com.java.codigo.examen_spring.util.UtilConver;
import com.java.codigo.examen_spring.util.UtilConverDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RedisService redisService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConsulApiReniec consulApiReniec;
    @Mock
    private ConsulPersonRedis consulPersonRedis;
    @Mock
    private UtilConverDto utilConverDto;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignIn_success() throws Exception {
        //ARRANGE
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("user@example.com");
        signInRequest.setPassword("password");
        UserEntity user = new UserEntity();
        String token = "valid-jwt-token";
        String mensaje = "Logeo satisfactoria";
        //CUANDO EJECUTES ... Debes devovler ....
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);
        //EJECUTAR
        SignInResponse response = authenticationService.signIn(signInRequest);
        // Assert
        assertEquals(token, response.getToken());
        assertEquals(mensaje, response.getMessage());
    }

    @Test
    void testSingInUserNoFount() throws Exception {
        //ARRANGE
        SignInRequest signInRequest = new SignInRequest();
        UserEntity user = new UserEntity();
        //CUANDO EJECUTES ... Debes devovler ....
        when(userRepository.findByEmail(signInRequest.getEmail())).thenReturn(Optional.empty());
        when(jwtService.generateToken(user)).thenReturn(anyString());

        //EJECUTAR
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> authenticationService.signIn(signInRequest));
        assertEquals("No se enceontró el usuario", throwable.getMessage());

    }

    @Test
    void testSigUpUser_success() throws Exception {
        //ARRANGE
        SignUpRequest signUpRequest = new SignUpRequest();
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        //Mokeando
        when(consulApiReniec.consultDataReniec(signUpRequest)).thenReturn(userEntity);
        when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(userDto);

        DataResponse dataResponse = authenticationService.sigUpUser(signUpRequest);

        assertNotNull(dataResponse);
        assertEquals(Constants.COD_OK_USER, dataResponse.getCode());
        assertTrue(dataResponse.getData().isPresent());
    }
    @Test
    void testSigUpUser_Error() throws Exception {
        //ARRANGE
        SignUpRequest signUpRequest = new SignUpRequest();
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        //Mokeando
        when(consulApiReniec.consultDataReniec(null)).thenReturn(userEntity);
        when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(userDto);

        Throwable throwable = assertThrows(ResourceNotFoundException.class, () ->
                authenticationService.sigUpUser(signUpRequest));
        assertEquals("No se encontró el dni en reniec", throwable.getMessage());

    }
    @Test
    void testSigUpAdmin_success() throws Exception {
        //ARRANGE
        SignUpRequest signUpRequest = new SignUpRequest();
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        //Mokeando
        when(consulApiReniec.consultDataReniec(signUpRequest)).thenReturn(userEntity);
        when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(userDto);

        DataResponse dataResponse = authenticationService.sigUpAdmin(signUpRequest);

        assertNotNull(dataResponse);
        assertEquals(Constants.COD_OK_USER, dataResponse.getCode());
        assertTrue(dataResponse.getData().isPresent());
    }

    @Test
    void testSigUpAdmin_Error() throws Exception {
        //ARRANGE
        SignUpRequest signUpRequest = new SignUpRequest();
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        //Mokeando
        when(consulApiReniec.consultDataReniec(null)).thenReturn(userEntity);
        when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(userDto);

        Throwable throwable = assertThrows(ResourceNotFoundException.class, () ->
                authenticationService.sigUpAdmin(signUpRequest));
        assertEquals("No se encontró el dni en reniec", throwable.getMessage());

    }

    @Test
    void testListUser_Succes() throws Exception {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        DataResponse dataResponse = authenticationService.listUsers();

        assertEquals(Constants.COD_OK_USER,dataResponse.getCode());
        assertEquals(Constants.MESS_OK_USER,dataResponse.getMessage());

    }

    @Test
    void testListUser_Error() throws Exception {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        DataResponse dataResponse = authenticationService.listUsers();

        assertEquals(Constants.COD_ERROR_NOTFOUND,dataResponse.getCode());
        assertEquals(Constants.MESS_ERROR_NOTFOUND,dataResponse.getMessage());
    }

    @Test
    void testFindUser_Success() throws Exception {
        String dni = "41736331";
        UserEntity userEntity = new UserEntity();
        UserDto responseDto = new UserDto();
        //userEntity.setNumeroDoc(dni);

        when(consulPersonRedis.findPersonRedis(dni)).thenReturn(Optional.of(userEntity));
        when(utilConverDto.getDataresponse(userEntity)).thenReturn(responseDto);

        DataResponse dataResponse = authenticationService.findUser(dni);

        assertEquals(Constants.COD_OK_USER,dataResponse.getCode());
        assertEquals(Constants.MESS_OK_USER,dataResponse.getMessage());
    }

    @Test
    void testFindUser_error() throws Exception {
        String dni = "41736331";
        UserEntity userEntity = new UserEntity();
        UserDto responseDto = new UserDto();

        when(consulPersonRedis.findPersonRedis(dni)).thenReturn(Optional.empty());

        DataResponse dataResponse = authenticationService.findUser(dni);

        assertEquals(Constants.COD_ERROR_NOTFOUND,dataResponse.getCode());
        assertEquals(Constants.MESS_ERROR_NOTFOUND,dataResponse.getMessage());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        Long id = 1l;
        UserRequest userRequest = new UserRequest();
        UserEntity userEntity = new UserEntity();
        UserDto responseDto = new UserDto();

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(responseDto);

        DataResponse dataResponse = authenticationService.updateUser(id,userRequest);

        assertEquals(Constants.COD_OK_USER,dataResponse.getCode());
        assertEquals(Constants.MESS_OK_USER,dataResponse.getMessage());
    }
    @Test
    void testUpdateUser_Error() throws Exception {
        Long id = 1l;
        UserRequest userRequest = new UserRequest();
        UserEntity userEntity = new UserEntity();
        UserDto responseDto = new UserDto();


    when(userRepository.findById(id)).thenReturn(Optional.empty());
    when(utilConverDto.getDataresponse(userRepository.save(userEntity))).thenReturn(responseDto);

    DataResponse dataResponse = authenticationService.updateUser(id,userRequest);

    assertEquals(Constants.COD_ERROR_NOTFOUND,dataResponse.getCode());
    assertEquals(Constants.MESS_ERROR_NOTFOUND,dataResponse.getMessage());
    }

    @Test
    void testDelete_Success() throws Exception {
        Long id=1l;
        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        doNothing().when(consulPersonRedis).deletePersonaRedis(userEntity.getNumeroDoc());

        DataResponse dataResponse = authenticationService.deleteUser(id);

        assertEquals(Constants.COD_OK_USER,dataResponse.getCode());
        assertEquals(Constants.MESS_OK_USER,dataResponse.getMessage());

    }
    @Test
    void testDelete_Error() throws Exception {
        Long id=1l;
        UserDto userDto = new UserDto();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        DataResponse dataResponse = authenticationService.deleteUser(id);

        assertEquals(Constants.COD_ERROR_NOTFOUND,dataResponse.getCode());
        assertEquals(Constants.MESS_ERROR_NOTFOUND,dataResponse.getMessage());

    }

}


    /*

        } else {
            return new DataResponse(Constants.COD_ERROR_NOTFOUND, Constants.MESS_NOT_FOUND, Optional.empty());
        }
    }

     */

/*





    @Test
    void testFindUserSuccess() throws Exception {
        String dni = "41736331";
        UserEntity userEntity = new UserEntity();
        AuthenticationServiceImpl spyService = spy(authenticationService);

        doReturn(Optional.of(userEntity)).when(spyService).getFindUserEntity(anyString());

        DataResponse response = spyService.findUser(dni);

        assertNotNull(response);
        assertTrue(response.getData().isPresent());
        assertEquals(response.getCode(), Constants.COD_OK_USER);
        assertEquals(response.getMessage(), Constants.MESS_OK_USER);
    }

    @Test
    void testFindUserNoFound() throws Exception {
        String dni = "41736331";
        UserEntity userEntity = new UserEntity();
        AuthenticationServiceImpl spyService = spy(authenticationService);

        doReturn(Optional.empty()).when(spyService).getFindUserEntity(anyString());
        DataResponse response = spyService.findUser(dni);

        assertNotNull(response);
        assertTrue(response.getData().isEmpty());
        assertEquals(Constants.COD_ERROR_NOTFOUND, response.getCode());
        assertEquals(Constants.MESS_NOT_FOUND, response.getMessage());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserEntity userEntity = new UserEntity();
        UserRequest userRequest = new UserRequest();
        Long id = 1l;
        userRequest.setTipoDocumento("DNI");
        userRequest.setNombres("Juan");
        userRequest.setApellidoPaterno("Perez");
        userRequest.setApellidoMaterno("Gomez");
        userRequest.setEmail("juan.perez@example.com");
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        DataResponse response = authenticationService.updateUser(id, userRequest);

        //UserDto userDto = (UserDto) response.getData().get();

        // Verifica los atributos de userDto según sea necesario


        assertEquals(Constants.COD_OK_USER, response.getCode());
        assertEquals(Constants.MESS_OK_USER, response.getMessage());
        assertTrue(response.getData().isPresent());
        assertNotNull(response);
    }

    @Test
    void testUpdateUserNoFound() throws Exception {
        UserEntity userEntity = new UserEntity();
        UserRequest userRequest = new UserRequest();
        Long id = 1l;
        userRequest.setTipoDocumento("DNI");
        userRequest.setNombres("Juan");
        userRequest.setApellidoPaterno("Perez");
        userRequest.setApellidoMaterno("Gomez");
        userRequest.setEmail("juan.perez@example.com");
        userEntity.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        DataResponse response = authenticationService.updateUser(id, userRequest);

        assertEquals(Constants.COD_ERROR_NOTFOUND, response.getCode());
        assertEquals(Constants.MESS_NOT_FOUND, response.getMessage());
        assertTrue(response.getData().isEmpty());
        assertNotNull(response);
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        UserEntity userEntity = new UserEntity();
        UserRequest userRequest = new UserRequest();
        Long id = 1l;
        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        DataResponse response = authenticationService.deleteUser(id);

        assertEquals(Constants.COD_OK_USER, response.getCode());
        assertEquals(Constants.MESS_OK_USER, response.getMessage());
        assertTrue(response.getData().isPresent());
        assertNotNull(response);
    }

    @Test
    void testDeleteUserError() throws Exception {
        UserEntity userEntity = new UserEntity();
        UserRequest userRequest = new UserRequest();
        Long id = 1l;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        DataResponse response = authenticationService.deleteUser(id);

        assertEquals(Constants.COD_ERROR_NOTFOUND, response.getCode());
        assertEquals(Constants.MESS_NOT_FOUND, response.getMessage());
        assertTrue(response.getData().isEmpty());
        assertNotNull(response);
    }

    @Test
    void testFindUserExistRedis() {
        String dni = "417363331";
        UserEntity userEntity = new UserEntity();
        userEntity.setNumeroDoc(dni);
        String redisData = "41736331";

        when(redisService.getDataRedis(dni)).thenReturn(redisData);
        PowerMockito.mockStatic(UtilConver.class);
        PowerMockito.when(UtilConver.converFromString(redisData, UserEntity.class)).thenReturn(userEntity);

        Optional<UserEntity> response = authenticationService.getFindUserEntity(dni);
        assertTrue(response.isPresent());
        assertEquals(dni, response.get().getNumeroDoc());
    }*/

/*
Optional<UserEntity> getFindUserEntity(String dni) {
        //consultando en redis para ver si existe
        String dataRedis = redisService.getDataRedis(dni);
        if (dataRedis == null) {
            Optional<UserEntity> userEntity = userRepository.findByNumeroDoc(dni);
            if (userEntity.isPresent()) {
                String stringRedis = UtilConver.converToString(userEntity.get());
                redisService.saveRedis(dni, stringRedis, Constants.EXPIRE_TIME);
            }
            return userEntity;
        } else {
            return Optional.of(UtilConver.converFromString(dataRedis, UserEntity.class));
        }
    }
 */