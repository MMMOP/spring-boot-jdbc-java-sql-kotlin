package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.management.timer.Timer;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        log.debug("init cache config");

        return new ConcurrentMapCacheManager(
            "ITEM",
                        "TIME"
                    );
    }

    @Scheduled(fixedRate = Timer.ONE_SECOND * 2)
    @Caching(evict = {
            @CacheEvict(cacheNames="ITEM", allEntries=true),
            @CacheEvict(cacheNames="TIME", allEntries=true)
    })
    public void clearAllCaches() {
        log.info("Clearing all caches entries");
    }
}