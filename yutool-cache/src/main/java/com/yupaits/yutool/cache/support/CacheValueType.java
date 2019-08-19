package com.yupaits.yutool.cache.support;

/**
 * Redis缓存数据类型
 * @author yupaits
 * @date 2019/7/17
 */
public enum CacheValueType {
    /**
     * List类型
     */
    LIST,
    /**
     * Value类型
     */
    VALUE,
    /**
     * Hash类型
     */
    HASH,
    /**
     * Set类型
     */
    SET,
    /**
     * ZSet类型
     */
    ZSET;
}
