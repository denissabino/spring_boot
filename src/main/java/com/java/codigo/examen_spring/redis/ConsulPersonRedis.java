package com.java.codigo.examen_spring.redis;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import com.java.codigo.examen_spring.entity.UserEntity;
import com.java.codigo.examen_spring.repository.UserRepository;
import com.java.codigo.examen_spring.util.UtilConver;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsulPersonRedis {

    private final RedisService redisService;
    private final UserRepository userRepository;
    private final UtilConver utilConver;

    public ConsulPersonRedis(RedisService redisService, UserRepository userRepository, UtilConver utilConver) {
        this.redisService = redisService;
        this.userRepository = userRepository;
        this.utilConver = utilConver;
    }

    public Optional<UserEntity> findPersonRedis(String dni) {
        String dataRedis = null;
        //consultando en redis para ver si existe
        dataRedis = redisService.getDataRedis(dni);
        System.out.println("imprimiendo valor " + dataRedis);

        if (dataRedis == null) {
            Optional<UserEntity> userEntity = userRepository.findByNumeroDoc(dni);
            if (userEntity.isPresent()) {
                String stringRedis = utilConver.converToString(userEntity.get());
                redisService.saveRedis(dni, stringRedis, Constants.EXPIRE_TIME);
            }
            return userEntity;
        } else {
            System.out.println("imprimiendo valor " + dataRedis);
            return Optional.of(utilConver.converFromString(dataRedis, UserEntity.class));
        }
    }

    public void deletePersonaRedis(String key){
        redisService.deleteDataRedis(key);
    }
}
