package com.yupaits.yutool.mq.support;

import com.yupaits.yutool.cache.support.CacheKeyGenerator;
import lombok.AllArgsConstructor;

/**
 * 重试消息体缓存key
 * @author yupaits
 * @date 2019/7/18
 */
@AllArgsConstructor
public class MessageCacheKey implements CacheKeyGenerator {
    private static final String MESSAGE_CACHE_KEY_PREFIX = "mq:retry:msg:";

    /**
     * 关联消息ID
     */
    private String correlationId;

    @Override
    public String cacheKey() {
        return MESSAGE_CACHE_KEY_PREFIX + correlationId;
    }
}
