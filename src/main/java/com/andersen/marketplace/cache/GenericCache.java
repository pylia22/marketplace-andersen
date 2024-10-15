package com.andersen.marketplace.cache;

import java.util.Optional;

public interface GenericCache <K, V> {

    void clean();

    void clear();

    Optional<V> get(K key);

    void put(K key, V value);

    void remove(K key);
}
