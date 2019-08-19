package com.yupaits.yutool.cache.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.yupaits.yutool.cache.config.CacheProperties;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存管理器
 * @author yupaits
 * @date 2019/7/17
 */
public class LocalCacheManager {
    private static final String PERMANENT_CACHE = "permanent";
    private final Map<String, Cache<String, Object>> cacheMap = Maps.newConcurrentMap();

    private final CacheProperties cacheProperties;

    public LocalCacheManager(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    /**
     * 永久缓存，无过期时间
     * @return 永久Cache
     */
    public Cache<String, Object> permanentCache() {
        if (cacheMap.containsKey(PERMANENT_CACHE) && cacheMap.get(PERMANENT_CACHE) != null) {
            return cacheMap.get(PERMANENT_CACHE);
        }
        Cache<String, Object> permanentCache = CacheBuilder.newBuilder().maximumSize(cacheProperties.getLocalMaxSize()).build();
        cacheMap.put(PERMANENT_CACHE, permanentCache);
        return permanentCache;
    }

    /**
     * 可过期缓存
     * @param timeout 过期时间数值
     * @param timeUnit 过期时间单位
     * @return 可过期Cache
     */
    public Cache<String, Object> expiredCache(long timeout, TimeUnit timeUnit) {
        String cacheMapKey = cacheMapKey(timeout, timeUnit);
        if (cacheMap.containsKey(cacheMapKey) && cacheMap.get(cacheMapKey) != null) {
            return cacheMap.get(cacheMapKey);
        }
        Cache<String, Object> expiredCache = CacheBuilder.newBuilder()
                .maximumSize(cacheProperties.getLocalMaxSize())
                .expireAfterWrite(timeout, timeUnit)
                .build();
        cacheMap.put(cacheMapKey, expiredCache);
        return expiredCache;
    }

    /**
     * 根据timeout和TimeUnit获取cacheMapKey
     * @param timeout timeout
     * @param timeUnit TimeUnit
     * @return cacheMapKey
     */
    private String cacheMapKey(long timeout, TimeUnit timeUnit) {
        return timeout + timeUnit.name();
    }
}
