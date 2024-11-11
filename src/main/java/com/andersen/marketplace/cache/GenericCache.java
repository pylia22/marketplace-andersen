package com.andersen.marketplace.cache;

import java.util.Optional;

/**
 * Generic cache interface for managing cache operations.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public interface GenericCache<K, V> {

    /**
     * Cleans the cache by removing expired or unnecessary entries.
     */
    void clean();

    /**
     * Clears all entries from the cache.
     */
    void clear();

    /**
     * Retrieves a value from the cache by its key.
     *
     * @param key the key whose associated value is to be returned
     * @return an Optional containing the value, if present
     */
    Optional<V> get(K key);

    /**
     * Puts a key-value pair into the cache.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    void put(K key, V value);

    /**
     * Removes a key-value pair from the cache by its key.
     *
     * @param key the key whose associated value is to be removed
     */
    void remove(K key);
}