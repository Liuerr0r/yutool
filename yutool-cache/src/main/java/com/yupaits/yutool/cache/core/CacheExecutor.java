package com.yupaits.yutool.cache.core;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 缓存操作执行器
 * @author yupaits
 * @date 2019/7/16
 */
@SuppressWarnings("unchecked")
public class CacheExecutor implements ICacheExecutor {
    private final RedisTemplate<String, Object> redisTemplate;
    private final LocalCacheManager localCacheManager;

    public CacheExecutor(RedisTemplate redisTemplate, LocalCacheManager localCacheManager) {
        this.redisTemplate = redisTemplate;
        this.localCacheManager = localCacheManager;
    }

    @Override
    public <K, V> void setCache(K key, V value, CacheProps cacheProps) throws CacheException {
        checkCacheProps(cacheProps);
        String keyStr = getKeyStr(key, cacheProps);
        if (cacheProps.isCacheLocal()) {
            setLocalCache(keyStr, value, cacheProps);
        }
        if (cacheProps.isCacheDistribute()) {
            setDistributedCache(keyStr, value, cacheProps);
        }
    }

    @Override
    public <K, V> V getCache(K key, CacheProps cacheProps) throws CacheException {
        checkCacheProps(cacheProps);
        String keyStr = getKeyStr(key, cacheProps);
        V result = null;
        if (cacheProps.isCacheLocal()) {
            result = getLocalCache(keyStr, cacheProps);
        }
        if (result == null && cacheProps.isCacheDistribute()) {
            result = getDistributedCache(keyStr, cacheProps);
            if (result != null && cacheProps.isCacheLocal()) {
                setLocalCache(keyStr, result, cacheProps);
            }
        }
        return result;
    }

    @Override
    public <K> void removeCache(K key, CacheProps cacheProps) throws CacheException {
        checkCacheProps(cacheProps);
        String keyStr = getKeyStr(key, cacheProps);
        if (cacheProps.isCacheLocal()) {
            removeLocalCache(keyStr, cacheProps);
        }
        if (cacheProps.isCacheDistribute()) {
            removeDistributedCache(keyStr, cacheProps);
        }
    }

    @Override
    public void removeCacheByPrefix(String keyPrefix, CacheProps cacheProps) throws CacheException {
        checkCacheProps(cacheProps);
        if (cacheProps.isCacheLocal()) {
            Cache<String, Object> cache;
            if (cacheProps.isExpired()) {
                cache = localCacheManager.expiredCache(cacheProps.getTimeout(), cacheProps.getTimeUnit());
            } else {
                cache = localCacheManager.permanentCache();
            }
            Set<String> keySet = cache.asMap().keySet();
            if (CollectionUtils.isNotEmpty(keySet)) {
                List<String> keys = keySet.stream().filter(key -> StringUtils.startsWithIgnoreCase(key, keyPrefix)).collect(Collectors.toList());
                cache.invalidateAll(keys);
            }
        }
        if (cacheProps.isCacheDistribute()) {
            Set<String> keys = redisTemplate.keys(keyPrefix + "*");
            if (CollectionUtils.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        }
    }

    @Override
    public <V> void setLocalCache(String keyStr, V value, CacheProps cacheProps) {
        if (cacheProps.isExpired()) {
            localCacheManager.expiredCache(cacheProps.getTimeout(), cacheProps.getTimeUnit()).put(keyStr, value);
        } else {
            localCacheManager.permanentCache().put(keyStr, value);
        }
    }

    @Override
    public <V> V getLocalCache(String key, CacheProps cacheProps) {
        if (cacheProps.isExpired()) {
            return (V) localCacheManager.expiredCache(cacheProps.getTimeout(), cacheProps.getTimeUnit()).getIfPresent(key);
        }
        return (V) localCacheManager.permanentCache().getIfPresent(key);
    }

    @Override
    public void removeLocalCache(String key, CacheProps cacheProps) throws CacheException {
        if (cacheProps.isExpired()) {
            localCacheManager.expiredCache(cacheProps.getTimeout(), cacheProps.getTimeUnit()).invalidate(key);
        } else {
            localCacheManager.permanentCache().invalidate(key);
        }
    }

    @Override
    public <V> void setDistributedCache(String keyStr, V value, CacheProps cacheProps) throws CacheException {
        CacheValueType valueType;
        if (cacheProps.getValueType() != null) {
            valueType = cacheProps.getValueType();
        } else {
             valueType =  TypeMapping.getValueType(value.getClass());
             cacheProps.setValueType(valueType);
        }
        switch (valueType) {
            case VALUE:
                redisTemplate.opsForValue().set(keyStr, value);
                break;
            case LIST:
                redisTemplate.opsForList().rightPushAll(keyStr, (List) value);
                break;
            case HASH:
                Map<Object, Object> map = (Map) value;
                for (Map.Entry entry : map.entrySet()) {
                    redisTemplate.opsForHash().put(keyStr, entry.getKey(), entry.getValue());
                }
                break;
            case SET:
                redisTemplate.opsForSet().add(keyStr, ((Set) value).toArray());
                break;
            case ZSET:
                SortedSet sortedSet = (SortedSet) value;
                Iterator iterator = sortedSet.iterator();
                double score = 0.0;
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    score++;
                    redisTemplate.opsForZSet().add(keyStr, obj, score);
                }
                break;
            default:
                throw new CacheException(String.format("不支持的Redis缓存数据类型：%s", valueType.name()));
        }
        if (cacheProps.isExpired()) {
            redisTemplate.expire(keyStr, cacheProps.getTimeout(), cacheProps.getTimeUnit());
        }
    }

    @Override
    public <V> V getDistributedCache(String key, CacheProps cacheProps) throws CacheException {
        Assert.notNull(cacheProps.getValueType(), "Redis缓存数据类型不能为空");
        switch (cacheProps.getValueType()) {
            case VALUE:
                return (V) redisTemplate.opsForValue().get(key);
            case LIST:
                return (V) redisTemplate.opsForList().range(key, 0, -1);
            case HASH:
                return (V) redisTemplate.opsForHash().multiGet(key, redisTemplate.opsForHash().keys(key));
            case SET:
                return (V) redisTemplate.opsForSet().members(key);
            case ZSET:
                return (V) redisTemplate.opsForZSet().range(key, 0, -1);
            default:
                throw new CacheException(String.format("不支持的Redis缓存数据类型：%s", cacheProps.getValueType().name()));
        }
    }

    @Override
    public void removeDistributedCache(String key, CacheProps cacheProps) {
        redisTemplate.delete(key);
    }

    /**
     * 校验CacheProps参数
     * @param cacheProps CacheProps参数
     * @throws CacheException 校验失败抛出CacheException
     */
    private void checkCacheProps(CacheProps cacheProps) throws CacheException {
        if (cacheProps == null || !cacheProps.isValid()) {
            throw new CacheException(String.format("缓存配置校验失败，配置信息：%s", cacheProps));
        }
    }

    /**
     * 获取缓存key字符串
     * @param key 缓存key
     * @param cacheProps CacheProps对象
     * @param <K> Key类型
     * @return 缓存key字符串
     */
    private <K> String getKeyStr(K key, CacheProps cacheProps) throws CacheException {
        CacheKeyType keyType = cacheProps.getKeyType() != null ? cacheProps.getKeyType() : CacheKeyType.STRING;
        switch (keyType) {
            case STRING:
                return (String) key;
            case OBJECT:
                CacheKeyStrategy keyStrategy = cacheProps.getKeyStrategy() != null ? cacheProps.getKeyStrategy() : CacheKeyStrategy.TO_STRING;
                switch (keyStrategy) {
                    case JSON:
                        return JSON.toJSONString(key);
                    case TO_STRING:
                        return key.toString();
                    case GENERATOR:
                        if (key instanceof CacheKeyGenerator) {
                            return ((CacheKeyGenerator) key).cacheKey();
                        }
                        throw new CacheException("当前的缓存Key类型不支持自定义缓存键生成策略");
                    default:
                        throw new CacheException(String.format("不支持的缓存键生成策略：%s", keyStrategy.name()));
                }
            default:
                throw new CacheException(String.format("不支持的缓存Key类型：%s", keyType.name()));
        }
    }
}
