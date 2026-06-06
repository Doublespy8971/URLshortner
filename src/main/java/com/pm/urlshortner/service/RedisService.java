package com.pm.urlshortner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Get value from Redis.
     * @param code key
     * @return originalUrl or null
     */
    public String get(String code) {
        return redisTemplate.opsForValue().get(code);
    }

    /**
     * Store value in Redis with default 24h TTL.
     * @param code key
     * @param originalUrl value
     */
    public void set(String code, String originalUrl) {
        redisTemplate.opsForValue().set(code, originalUrl, Duration.ofHours(24));
    }

    /**
     * Store value in Redis with custom TTL.
     * @param code key
     * @param originalUrl value
     * @param ttl custom duration
     */
    public void set(String code, String originalUrl, Duration ttl) {
        redisTemplate.opsForValue().set(code, originalUrl, ttl);
    }

    /**
     * Delete key from Redis.
     * @param code key
     */
    public void delete(String code) {
        redisTemplate.delete(code);
    }

    /**
     * Check if key exists in Redis.
     * @param code key
     * @return boolean
     */
    public boolean exists(String code) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(code));
    }
}


