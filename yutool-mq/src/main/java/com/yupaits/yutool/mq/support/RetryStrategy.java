package com.yupaits.yutool.mq.support;

/**
 * 重试策略
 * @author yupaits
 * @date 2019/7/19
 */
public enum RetryStrategy {
    /**
     * 周期性
     */
    PERIODIC,
    /**
     * 渐进式
     */
    PROGRESSIVE;
}
