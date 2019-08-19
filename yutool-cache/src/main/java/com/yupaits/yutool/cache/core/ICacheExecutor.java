package com.yupaits.yutool.cache.core;


import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.CacheProps;

/**
 * 缓存操作执行器接口
 * @author yupaits
 * @date 2019/7/16
 */
public interface ICacheExecutor {

    /**
     * 根据CacheProps设置缓存
     * @param key 缓存key
     * @param value 缓存value
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @param <V> Value类型
     * @throws CacheException 抛出CacheException
     */
    <K, V> void setCache(K key, V value, CacheProps cacheProps) throws CacheException;

    /**
     * 根据CacheProps获取缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @param <V> Value类型
     * @return 缓存value
     * @throws CacheException 抛出CacheException
     */
    <K, V> V getCache(K key, CacheProps cacheProps) throws CacheException;

    /**
     * 根据CacheProps删除缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @throws CacheException 抛出CacheException
     */
    <K> void removeCache(K key, CacheProps cacheProps) throws CacheException;

    /**
     * 根据CacheProps和Key前缀模糊删除缓存
     * @param keyPrefix 缓存key前缀
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    void removeCacheByPrefix(String keyPrefix, CacheProps cacheProps) throws CacheException;

    /**
     * 设置本地缓存
     * @param key 缓存key
     * @param value 缓存value
     * @param <V> Value类型
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    <V> void setLocalCache(String key, V value, CacheProps cacheProps) throws CacheException;

    /**
     * 获取本地缓存
     * @param key 缓存key
     * @param <V> Value类型
     * @param cacheProps CacheProps对象
     * @return 缓存value
     * @throws CacheException 抛出CacheException
     */
    <V> V getLocalCache(String key, CacheProps cacheProps) throws CacheException;

    /**
     * 删除本地缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    void removeLocalCache(String key, CacheProps cacheProps) throws CacheException;

    /**
     * 设置分布式缓存
     * @param key 缓存key
     * @param value 缓存value
     * @param <V> Value类型
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    <V> void setDistributedCache(String key, V value, CacheProps cacheProps) throws CacheException;

    /**
     * 获取分布式缓存
     * @param key 缓存key
     * @param <V> Value类型
     * @param cacheProps CacheProps对象
     * @return 缓存value
     * @throws CacheException 抛出CacheException
     */
    <V> V getDistributedCache(String key, CacheProps cacheProps) throws CacheException;

    /**
     * 删除分布式缓存
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @throws CacheException 抛出CacheException
     */
    void removeDistributedCache(String key, CacheProps cacheProps) throws CacheException;
}
