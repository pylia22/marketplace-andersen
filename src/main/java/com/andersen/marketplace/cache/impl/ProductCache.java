package com.andersen.marketplace.cache.impl;

import com.andersen.marketplace.cache.GenericCache;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Cache implementation for managing products.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
@Component
public class ProductCache<K, V> implements GenericCache<K, V> {

    public static final Long DEFAULT_CACHE_TIMEOUT = 90000L;
    protected Map<K, ProductCache.CacheValue<V>> productCache;
    protected Long cacheTimeout;

    /**
     * Constructs a new ProductCache with the default cache timeout.
     */
    public ProductCache() {
        this(DEFAULT_CACHE_TIMEOUT);
    }

    /**
     * Constructs a new ProductCache with a specified cache timeout.
     *
     * @param cacheTimeout the cache timeout in milliseconds
     */
    public ProductCache(Long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        this.clear();
    }

    /**
     * Cleans the cache by removing expired entries.
     */
    @Override
    public void clean() {
        for (K key : this.getExpiredKeys()) {
            this.remove(key);
        }
    }

    /**
     * Retrieves the set of expired keys.
     *
     * @return a set of expired keys
     */
    protected Set<K> getExpiredKeys() {
        return this.productCache.keySet().parallelStream()
                .filter(this::isExpired)
                .collect(Collectors.toSet());
    }

    /**
     * Checks if a key is expired.
     *
     * @param key the key to check
     * @return true if the key is expired, false otherwise
     */
    protected boolean isExpired(K key) {
        LocalDateTime expirationDateTime = this.productCache.get(key).getCreatedAt().plus(this.cacheTimeout, ChronoUnit.MILLIS);
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    /**
     * Clears all entries from the cache.
     */
    @Override
    public void clear() {
        this.productCache = new HashMap<>();
    }

    /**
     * Retrieves a value from the cache by its key.
     *
     * @param key the key whose associated value is to be returned
     * @return an Optional containing the value, if present
     */
    @Override
    public Optional<V> get(K key) {
        this.clean();
        return Optional.ofNullable(this.productCache.get(key)).map(ProductCache.CacheValue::getValue);
    }

    /**
     * Puts a key-value pair into the cache.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    @Override
    public void put(K key, V value) {
        this.productCache.put(key, this.createCacheValue(value));
    }

    /**
     * Creates a cache value with the current timestamp.
     *
     * @param value the value to be cached
     * @return the cache value
     */
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

    /**
     * Removes a key-value pair from the cache by its key.
     *
     * @param key the key whose associated value is to be removed
     */
    @Override
    public void remove(K key) {
        this.productCache.remove(key);
    }

    /**
     * Interface for cache values.
     *
     * @param <V> the type of the value
     */
    protected interface CacheValue<V> {
        V getValue();

        LocalDateTime getCreatedAt();
    }
}
