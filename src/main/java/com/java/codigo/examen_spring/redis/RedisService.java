package com.java.codigo.examen_spring.redis;

import com.java.codigo.examen_spring.aggregates.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveRedis(String key, String value, int expTime) {
        redisTemplate.opsForValue().set(Constants.REDIS_KEY_API + key, value);
        redisTemplate.expire(key, expTime, TimeUnit.MINUTES);
    }

    public String getDataRedis(String key) {
        return redisTemplate.opsForValue().get(Constants.REDIS_KEY_API + key);
    }

    public void deleteDataRedis(String key) {
        redisTemplate.delete(Constants.REDIS_KEY_API + key);
    }
}
