package com.andersen.marketplace.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class GenericCacheImpl<K, V> implements GenericCache<K, V> {

    public static final Long DEFAULT_CACHE_TIMEOUT = 120000L;
    protected Map<K, CacheValue<V>> cacheMap;
    protected Long cacheTimeout;

    public GenericCacheImpl() {
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public GenericCacheImpl(Long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        this.clear();
    }

    @Override
    public void clean() {
        for(K key: this.getExpiredKeys()) {
            this.remove(key);
        }
    }

    protected Set<K> getExpiredKeys() {
        return this.cacheMap.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(toSet());
    }

    protected boolean isExpired(K key) {
        LocalDateTime expirationDateTime = this.cacheMap.get(key).getCreatedAt().plus(this.cacheTimeout, ChronoUnit.MILLIS);
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    @Override
    public void clear() {
        this.cacheMap = new HashMap<>();
    }

    @Override
    public Optional<V> get(K key) {
        this.clean();
        return Optional.ofNullable(this.cacheMap.get(key)).map(CacheValue::getValue);
    }

    @Override
    public void put(K key, V value) {
        this.cacheMap.put(key, this.createCacheValue(value));
    }

    protected CacheValue<V> createCacheValue(V value) {
        LocalDateTime now = LocalDateTime.now();
        return new CacheValue<V>() {
            @Override
            public V getValue() {
                return value;
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return now;
            }
        };
    }

    @Override
    public void remove(K key) {
        this.cacheMap.remove(key);
    }

    protected interface CacheValue<V> {
        V getValue();

        LocalDateTime getCreatedAt();
    }
}
