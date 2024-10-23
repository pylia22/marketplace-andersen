package com.andersen.marketplace.cache.impl;

import com.andersen.marketplace.cache.GenericCache;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
public class ProductCache <K, V> implements GenericCache<K, V> {
    public static final Long DEFAULT_CACHE_TIMEOUT = 90000L;
    protected Map<K, ProductCache.CacheValue<V>> productCache;
    protected Long cacheTimeout;

    public ProductCache() {
        this(DEFAULT_CACHE_TIMEOUT);
    }

    public ProductCache(Long cacheTimeout) {
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
        return this.productCache.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(toSet());
    }

    protected boolean isExpired(K key) {
        LocalDateTime expirationDateTime = this.productCache.get(key).getCreatedAt().plus(this.cacheTimeout, ChronoUnit.MILLIS);
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    @Override
    public void clear() {
        this.productCache = new HashMap<>();
    }

    @Override
    public Optional<V> get(K key) {
        this.clean();
        return Optional.ofNullable(this.productCache.get(key)).map(ProductCache.CacheValue::getValue);
    }

    @Override
    public void put(K key, V value) {
        this.productCache.put(key, this.createCacheValue(value));
    }

    protected ProductCache.CacheValue<V> createCacheValue(V value) {
        LocalDateTime now = LocalDateTime.now();
        return new ProductCache.CacheValue<V>() {
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
        this.productCache.remove(key);
    }

    protected interface CacheValue<V> {
        V getValue();

        LocalDateTime getCreatedAt();
    }
}
