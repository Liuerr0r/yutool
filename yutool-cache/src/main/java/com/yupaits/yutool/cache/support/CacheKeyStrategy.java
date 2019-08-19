package com.yupaits.yutool.cache.support;

/**
 * 缓存键生成策略
 * @author yupaits
 * @date 2019/7/17
 */
public enum CacheKeyStrategy {
    /**
     * JSON字符串
     */
    JSON,
    /**
     * 使用toString方法
     */
    TO_STRING,
    /**
     * 继承CacheKeyGenerator，使用自定义生成方法
     */
    GENERATOR;
}
