package com.sectong.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by xueyong on 16/7/14.
 * mobileeasy.
 */
@SuppressWarnings("unchecked")
@Component
public class RedisUtil {

    private static final String PRODUCT_PREFIX = "ILIVE_";
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(PRODUCT_PREFIX + key);
        }
    }
    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(PRODUCT_PREFIX + key);
        return result;
    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(PRODUCT_PREFIX + key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(PRODUCT_PREFIX + key, value);
            redisTemplate.expire(PRODUCT_PREFIX + key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 右入队列
     * @param key
     * @param value
     */
    public void rpush(final String key, String value) {
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.getOperations().opsForList().rightPush(PRODUCT_PREFIX + key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 左出队列
     * @param key
     * @return
     */
    public Object lpop(final String key) {
        Object value = null;
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            value = operations.getOperations().opsForList().leftPop(PRODUCT_PREFIX + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}