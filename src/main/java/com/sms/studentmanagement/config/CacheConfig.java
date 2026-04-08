package com.sms.studentmanagement.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.sms.studentmanagement.constants.AppConstants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine-backed in-process cache configuration.
 *
 * <ul>
 *   <li>departments  – rarely change, cached for 10 min</li>
 *   <li>courses      – cached for 5 min</li>
 *   <li>students     – cached for 2 min (higher churn)</li>
 *   <li>dashboard    – cached for 1 min (stats)</li>
 * </ul>
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                AppConstants.CACHE_DEPARTMENTS,
                AppConstants.CACHE_COURSES,
                AppConstants.CACHE_STUDENTS,
                AppConstants.CACHE_DASHBOARD
        );
        manager.setCaffeine(caffeineCacheBuilder());
        return manager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats();
    }
}
