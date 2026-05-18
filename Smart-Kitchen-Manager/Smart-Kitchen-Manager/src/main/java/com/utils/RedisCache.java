package com.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 * 
 * 封装常用的Redis操作，提供简洁的API
 * 
 * @author 
 * @date 2023-04-25
 */
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存（永久）
     * 
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存（带过期时间）
     * 
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取缓存
     * 
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存（指定类型）
     * 
     * @param key 键
     * @param clazz 值的类型
     * @param <T> 泛型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return value == null ? null : (T) value;
    }

    /**
     * 删除缓存
     * 
     * @param key 键
     * @return 是否删除成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除缓存
     * 
     * @param keys 键集合
     * @return 删除的数量
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 判断键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * 
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     * 
     * @param key 键
     * @param unit 时间单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 根据模式删除键
     * 
     * @param pattern 模式（如：user:*）
     * @return 删除的数量
     */
    public Long deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            return redisTemplate.delete(keys);
        }
        return 0L;
    }

    /**
     * 根据模式查找键
     * 
     * @param pattern 模式（如：user:*）
     * @return 键集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 递增
     * 
     * @param key 键
     * @param delta 增量
     * @return 递增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * 
     * @param key 键
     * @param delta 减量
     * @return 递减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * Hash设置
     * 
     * @param key 键
     * @param hashKey Hash键
     * @param value 值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * Hash获取
     * 
     * @param key 键
     * @param hashKey Hash键
     * @return 值
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * Hash批量设置
     * 
     * @param key 键
     * @param map 值映射
     */
    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Hash获取所有
     * 
     * @param key 键
     * @return 值映射
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * Hash删除
     * 
     * @param key 键
     * @param hashKeys Hash键数组
     * @return 删除的数量
     */
    public Long hDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * Hash判断是否存在
     * 
     * @param key 键
     * @param hashKey Hash键
     * @return 是否存在
     */
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * List右侧推入
     * 
     * @param key 键
     * @param value 值
     * @return 列表长度
     */
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * List左侧推入
     * 
     * @param key 键
     * @param value 值
     * @return 列表长度
     */
    public Long lPushLeft(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * List获取范围
     * 
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return 值列表
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * List获取长度
     * 
     * @param key 键
     * @return 列表长度
     */
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * Set添加
     * 
     * @param key 键
     * @param values 值数组
     * @return 添加的数量
     */
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * Set获取所有成员
     * 
     * @param key 键
     * @return 成员集合
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * Set判断是否存在
     * 
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * Set获取大小
     * 
     * @param key 键
     * @return 集合大小
     */
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * Set移除
     * 
     * @param key 键
     * @param values 值数组
     * @return 移除的数量
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }
}
