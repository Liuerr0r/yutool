package com.yupaits.yutool.cache.annotation;


import com.yupaits.yutool.cache.support.CacheKeyStrategy;
import com.yupaits.yutool.cache.support.CacheKeyType;
import com.yupaits.yutool.cache.support.CacheValueType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存启用标识注解
 * @see com.yupaits.yutool.cache.support.CacheProps
 * @author yupaits
 * @date 2019/7/18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableCache {

    /**
     * 是否设置本地缓存
     */
    boolean local() default true;

    /**
     * 是否设置分布式缓存
     */
    boolean distribute() default true;

    /**
     * 是否会过期
     */
    boolean expired() default true;

    /**
     * 缓存过期时间数值
     */
    long timeout() default 30;

    /**
     * 缓存过期时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 缓存键类型
     */
    CacheKeyType keyType() default CacheKeyType.STRING;

    /**
     * 缓存键生成策略，缓存键类型为OBJECT时定义生成缓存键字符串的策略
     */
    CacheKeyStrategy keyStrategy() default CacheKeyStrategy.TO_STRING;

    /**
     * 缓存值类型
     */
    CacheValueType valueType() default CacheValueType.VALUE;
}
