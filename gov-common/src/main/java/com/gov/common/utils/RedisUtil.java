package com.gov.common.utils;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * 封装常用 Redis 操作：String、Hash、List、Set、ZSet、过期时间等
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== Key 操作 ====================

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置 key 过期时间（秒）
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * 设置 key 过期时间（自定义时间单位）
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
    }

    /**
     * 获取 key 剩余过期时间（秒）
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire != null ? expire : -1;
    }

    /**
     * 删除单个 key
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 批量删除 key
     */
    public long delete(Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count != null ? count : 0;
    }

    /**
     * 按模式删除 key（如 "user:*"）
     */
    public long deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            return delete(keys);
        }
        return 0;
    }

    // ==================== String 操作 ====================

    /**
     * 设置字符串值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串值并指定过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置字符串值并指定过期时间（自定义单位）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 仅当 key 不存在时设置值（分布式锁）
     */
    public boolean setIfAbsent(String key, Object value, long timeout) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS)
        );
    }

    /**
     * 获取字符串值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 自增（步长1）
     */
    public long incr(String key) {
        Long result = redisTemplate.opsForValue().increment(key);
        return result != null ? result : 0;
    }

    /**
     * 自增（自定义步长）
     */
    public long incrBy(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0;
    }

    /**
     * 自减
     */
    public long decr(String key) {
        Long result = redisTemplate.opsForValue().decrement(key);
        return result != null ? result : 0;
    }

    // ==================== Hash 操作 ====================

    /**
     * 设置 Hash 字段值
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量设置 Hash
     */
    public void hSetAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 Hash 字段值
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String hashKey) {
        return (T) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取整个 Hash
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 判断 Hash 字段是否存在
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 删除 Hash 字段
     */
    public long hDelete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * Hash 自增
     */
    public long hIncrBy(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    // ==================== List 操作 ====================

    /**
     * 右推入（添加到列表尾部）
     */
    public long lPush(String key, Object value) {
        Long result = redisTemplate.opsForList().rightPush(key, value);
        return result != null ? result : 0;
    }

    /**
     * 左推入
     */
    public long lLeftPush(String key, Object value) {
        Long result = redisTemplate.opsForList().leftPush(key, value);
        return result != null ? result : 0;
    }

    /**
     * 左弹出
     */
    @SuppressWarnings("unchecked")
    public <T> T lLeftPop(String key) {
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 右弹出
     */
    @SuppressWarnings("unchecked")
    public <T> T lRightPop(String key) {
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取列表指定范围元素
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表长度
     */
    public long lSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size != null ? size : 0;
    }

    /**
     * 获取列表指定索引元素
     */
    @SuppressWarnings("unchecked")
    public <T> T lIndex(String key, long index) {
        return (T) redisTemplate.opsForList().index(key, index);
    }

    // ==================== Set 操作 ====================

    /**
     * 添加 Set 元素
     */
    public long sAdd(String key, Object... values) {
        Long result = redisTemplate.opsForSet().add(key, values);
        return result != null ? result : 0;
    }

    /**
     * 获取 Set 所有元素
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断是否为 Set 成员
     */
    public boolean sIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 获取 Set 大小
     */
    public long sSize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size : 0;
    }

    /**
     * 移除 Set 元素
     */
    public long sRemove(String key, Object... values) {
        Long result = redisTemplate.opsForSet().remove(key, values);
        return result != null ? result : 0;
    }

    // ==================== ZSet（有序集合）操作 ====================

    /**
     * 添加 ZSet 元素
     */
    public boolean zAdd(String key, Object value, double score) {
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
    }

    /**
     * 按分数范围获取 ZSet 元素（升序）
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * ZSet 分数自增
     */
    public double zIncrBy(String key, Object value, double delta) {
        Double result = redisTemplate.opsForZSet().incrementScore(key, value, delta);
        return result != null ? result : 0;
    }

    /**
     * 移除 ZSet 元素
     */
    public long zRemove(String key, Object... values) {
        Long result = redisTemplate.opsForZSet().remove(key, values);
        return result != null ? result : 0;
    }
}
