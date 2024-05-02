package com.poluectov.rvproject.service.redis;

public interface RedisCacheService<K, V> {

    V get(K key);
    void put(K key, V value);

    void delete(K key);
}
