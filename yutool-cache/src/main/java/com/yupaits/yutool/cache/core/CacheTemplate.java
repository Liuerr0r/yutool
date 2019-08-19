package com.yupaits.yutool.cache.core;

import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.CacheProps;

/**
 * 缓存工具类
 * @author yupaits
 * @date 2019/7/16
 */
public class CacheTemplate {

    private final ICacheExecutor cacheExecutor;

    public CacheTemplate(ICacheExecutor cacheExecutor) {
        this.cacheExecutor = cacheExecutor;
    }

    /**
     * 设置缓存
     * @param key 缓存key
     * @param value 缓存value
     * @param <K> Key类型
     * @param <V> Value类型
     * @throws CacheException 抛出CacheException
     */
    public <K, V> void setCache(K key, V value) throws CacheException {
        setCache(key, value, CacheProps.defaultCacheProps());
    }

    /**
     * 根据CacheProps设置缓存
     * @param key 缓存key
     * @param value 缓存value
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @param <V> Value类型
     * @throws CacheException 抛出CacheException
     */
    public <K, V> void setCache(K key, V value, CacheProps cacheProps) throws CacheException {
        cacheExecutor.setCache(key, value, cacheProps);
    }

    /**
     * 根据Key获取缓存
     * @param key 缓存key
     * @param <K> Key类型
     * @param <V> Value类型
     * @return 缓存value
     * @throws CacheException 抛出CacheException
     */
    public <K, V> V getCache(K key) throws CacheException {
        return getCache(key, CacheProps.defaultCacheProps());
    }

    /**
     * 根据Key和CacheProps获取缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @param <V> Value类型
     * @return 缓存value
     * @throws CacheException 抛出CacheException
     */
    public <K, V> V getCache(K key, CacheProps cacheProps) throws CacheException {
        return cacheExecutor.getCache(key, cacheProps);
    }

    /**
     * 根据Key删除缓存
     * @param key 缓存key
     * @param <K> Key类型
     * @throws CacheException 抛出CacheException
     */
    public <K> void removeCache(K key) throws CacheException {
        removeCache(key, CacheProps.defaultCacheProps());
    }

    /**
     * 根据Key和CacheProps删除缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @throws CacheException 抛出CacheException
     */
    public <K> void removeCache(K key, CacheProps cacheProps) throws CacheException {
        cacheExecutor.removeCache(key, cacheProps);
    }

    /**
     * 根据Key前缀模糊删除缓存
     * @param keyPrefix 缓存key前缀
     * @throws CacheException 抛出CacheException
     */
    public void removeCacheByPrefix(String keyPrefix) throws CacheException {
        removeCacheByPrefix(keyPrefix, CacheProps.defaultCacheProps());
    }

    /**
     * 根据CacheProps和Key前缀模糊删除缓存
     * @param keyPrefix 缓存key前缀
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    public void removeCacheByPrefix(String keyPrefix, CacheProps cacheProps) throws CacheException {
        cacheExecutor.removeCacheByPrefix(keyPrefix, cacheProps);
    }
}
