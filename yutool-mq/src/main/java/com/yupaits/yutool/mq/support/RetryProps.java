package com.yupaits.yutool.mq.support;

import com.yupaits.yutool.cache.support.CacheKeyType;
import com.yupaits.yutool.cache.support.CacheProps;
import com.yupaits.yutool.cache.support.CacheValueType;
import com.yupaits.yutool.mq.core.IQueueEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 重试队列配置
 * @author yupaits
 * @date 2019/7/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryProps implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否重试
     */
    private boolean retryable;

    /**
     * 消息CorrelationId
     */
    private String correlationId;

    /**
     * 消息队列枚举
     */
    private IQueueEnum queueEnum;

    /**
     * 重试次数
     */
    private int times;

    /**
     * 重试策略
     */
    private RetryStrategy strategy;

    /**
     * 首次重试延时毫秒数
     */
    private long firstDelayMillis;

    /**
     * 首次重试已完成
     */
    private boolean firstDone;

    /**
     * 重试间隔时间毫秒数
     */
    private long intervalMillis;

    /**
     * 延时时间毫秒数集合
     */
    private List<Long> delays;

    /**
     * 重试消息缓存配置
     */
    private CacheProps cacheProps;

    /**
     * 参数校验
     * @return 校验通过
     */
    public boolean isValid() {
        return !retryable || queueEnum != null && strategy != null
                && ((strategy == RetryStrategy.PERIODIC && firstDelayMillis >= 0L && intervalMillis > 0L)
                || (strategy == RetryStrategy.PROGRESSIVE && delays != null && delays.size() == times));
    }

    /**
     * 设置默认的重试消息缓存配置
     */
    public void setDefaultCacheProps() {
        this.setCacheProps(CacheProps.builder()
                .cacheLocal(false)
                .cacheDistribute(true)
                .expired(true)
                .timeout(10000L)
                .timeUnit(TimeUnit.MILLISECONDS)
                .keyType(CacheKeyType.STRING)
                .valueType(CacheValueType.VALUE)
                .build());
    }
}
