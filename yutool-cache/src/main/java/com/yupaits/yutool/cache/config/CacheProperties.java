package com.yupaits.yutool.cache.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存自动装配配置
 * @author yupaits
 * @date 2019/7/17
 */
@Data
@ConfigurationProperties(prefix = "cache", ignoreInvalidFields = true)
public class CacheProperties {
    /**
     * 是否启用缓存组件
     */
    private boolean enabled;

    /**
     * 本地缓存最大数量
     */
    private long localMaxSize = 1000;

    /**
     * 缓存键前缀
     */
    private String keyPrefix = StringUtils.EMPTY;

    /**
     * 是否启用缓存Key布隆过滤器
     */
    private boolean bloomFilterEnabled = true;
}
