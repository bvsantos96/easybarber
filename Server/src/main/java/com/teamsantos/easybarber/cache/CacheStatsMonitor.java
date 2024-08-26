package com.teamsantos.easybarber.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheStatsMonitor {

    @Autowired
    private CacheManager cacheManager;

    public void printCacheStats() {
        Cache cache = cacheManager.getCache("employee");
        if (cache != null && cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache
                    .getNativeCache();
            System.out.println("Cache Stats: " + caffeineCache.stats());
        }
    }
}
