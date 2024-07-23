package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/test/user")
    public String getMyInfo(Authentication auth, Principal principal) {
        return "MobileInformation: " +
                principal.getName() +
                "\n" +
                "Authorities: " +
                auth.getAuthorities();
    }

    @GetMapping("/test/cache")
    public String getCacheStats() {
        Cache cache = cacheManager.getCache("employee");
        if (cache != null && cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
            com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = (com.github.benmanes.caffeine.cache.Cache<Object, Object>) cache
                    .getNativeCache();
            return "Cache Stats: " + caffeineCache.stats();
        }
        return "Cache not found";
    }
}
