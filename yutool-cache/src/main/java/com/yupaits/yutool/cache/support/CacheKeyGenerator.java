package com.yupaits.yutool.cache.support;

/**
 * 缓存Key生成接口
 * @author yupaits
 * @date 2019/7/17
 */
public interface CacheKeyGenerator {

    /**
     * 缓存key生成方法
     * @return 缓存key
     */
    String cacheKey();
}
