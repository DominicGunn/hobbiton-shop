package com.hobbiton.shop.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class LRUCacheTest {

    @Test
    public void testShouldPopulateCache() throws Exception {
        final LRUCache<Integer, UUID> lruCache = generateCacheElements(50, 25);

        assertThat(lruCache).isNotNull();
        assertThat(lruCache).hasSize(25);
    }

    @Test
    public void testCacheShouldNotExceedMaximumSize() throws Exception {
        final LRUCache<Integer, UUID> lruCache = generateCacheElements(50, 150);

        assertThat(lruCache).isNotNull();
        assertThat(lruCache).hasSize(50);
    }

    @Test
    public void testShouldNotRemoveActivelyUsedKey() throws Exception {
        // Generate a Cache with 10 elements.
        final LRUCache<Integer, UUID> lruCache = generateCacheElements(10, 10);

        // Make use of element in position 0, this is now actively used.
        final UUID firstUUID = lruCache.get(0);

        // Add 5 more elements to the cache
        lruCache.putAll(generateCacheElements(5, 5));

        // Ensure cache still contains the element that was previously at position 0.
        assertThat(lruCache.containsValue(firstUUID));
    }

    private LRUCache<Integer, UUID> generateCacheElements(int maxCacheSize, int totalElements) {
        final LRUCache<Integer, UUID> lruCache = new LRUCache<>(maxCacheSize);
        for (int i = 0; i < totalElements; i++) {
            lruCache.put(i, UUID.randomUUID());
        }
        return lruCache;
    }
}
