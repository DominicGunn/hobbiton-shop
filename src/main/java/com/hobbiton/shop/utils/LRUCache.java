package com.hobbiton.shop.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple least recently used Cache that relies on LinkedHashMap implementation to determine
 * which element is the eldest and should be removed.
 * @author Dominic Gunn
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int cacheSize;

    public LRUCache(int cacheSize) {
        super(cacheSize + 1, 1.0f, true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return super.size() > cacheSize;
    }
}
