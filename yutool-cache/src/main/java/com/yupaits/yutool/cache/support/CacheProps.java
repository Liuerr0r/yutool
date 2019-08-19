package com.yupaits.yutool.cache.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 * @author yupaits
 * @date 2019/7/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheProps implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否设置本地缓存
     */
    private boolean cacheLocal;

    /**
     * 是否设置分布式缓存
     */
    private boolean cacheDistribute;

    /**
     * 是否会过期
     */
    private boolean expired;

    /**
     * 缓存过期时间数值
     */
    private long timeout;

    /**
     * 缓存过期时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 缓存键类型
     */
    private CacheKeyType keyType;

    /**
     * 缓存键生成策略，缓存键类型为OBJECT时定义生成缓存键字符串的策略
     */
    private CacheKeyStrategy keyStrategy;

    /**
     * 缓存值类型
     */
    private CacheValueType valueType;

    public static CacheProps defaultCacheProps() {
        return CacheProps.builder()
                .cacheLocal(true)
                .cacheDistribute(true)
                .expired(true)
                .timeout(30)
                .timeUnit(TimeUnit.MINUTES)
                .keyType(CacheKeyType.STRING)
                .keyStrategy(CacheKeyStrategy.TO_STRING)
                .valueType(CacheValueType.VALUE)
                .build();
    }

    /**
     * 校验CacheProps是否有效
     * @return 校验结果
     */
    public boolean isValid() {
        return (cacheLocal || cacheDistribute) && (!expired || (timeout > 0 && timeUnit != null)) && keyType != null
                && valueType != null && (keyType != CacheKeyType.OBJECT || keyStrategy != null);
    }
}
