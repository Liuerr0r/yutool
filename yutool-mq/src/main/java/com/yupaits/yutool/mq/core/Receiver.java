package com.yupaits.yutool.mq.core;

import com.alibaba.fastjson.JSON;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.CacheProps;
import com.yupaits.yutool.mq.exception.MqRetryException;
import com.yupaits.yutool.mq.support.RetryProps;
import com.yupaits.yutool.mq.support.RetryPropsCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息接收
 * @author yupaits
 * @date 2019/7/18
 */
@Slf4j
public abstract class Receiver<M extends Serializable> {
    private RetryableSender sender;
    private CacheTemplate cacheTemplate;

    protected Receiver(RetryableSender sender, CacheTemplate cacheTemplate) {
        this.sender = sender;
        this.cacheTemplate = cacheTemplate;
    }

    /**
     * 接收并处理消息
     * @param message 消息体
     */
    protected void handle(@Payload M message) {
    }

    /**
     * 接收并处理消息
     * @param message 消息体
     * @param headers 消息头
     */
    protected void handle(@Payload M message, @Headers Map<String, Object> headers) {
    }

    /**
     * 根据消息头判断该消息是否为可重试消息
     * @param headers 消息头
     * @return 是否可重试
     */
    protected boolean isRetryable(Map<String, Object> headers) {
        return StringUtils.isNotBlank(MapUtils.getString(headers, RetryableSenderImpl.PROPS_KEY_HEADER));
    }

    /**
     * 消息重试
     * @param message 消息体
     * @param headers 消息头
     */
    protected void retry(M message, Map<String, Object> headers) throws MqRetryException {
        RetryProps retryProps;
        try {
            CacheProps cacheProps = JSON.parseObject((String) headers.get(RetryableSenderImpl.CACHE_PROPS_HEADER), CacheProps.class);
            retryProps = cacheTemplate.getCache(headers.get(RetryableSenderImpl.PROPS_KEY_HEADER), cacheProps);
            if (retryProps.isRetryable() && retryProps.getTimes() > 0) {
                retryProps.setTimes(retryProps.getTimes() - 1);
                String propsKey = new RetryPropsCacheKey(retryProps.getCorrelationId()).cacheKey();
                cacheTemplate.setCache(propsKey, retryProps, retryProps.getCacheProps());
                long delayMillis;
                switch (retryProps.getStrategy()) {
                    case PERIODIC:
                        if (retryProps.isFirstDone()) {
                            delayMillis = retryProps.getIntervalMillis();
                        } else {
                            retryProps.setFirstDone(true);
                            delayMillis = retryProps.getFirstDelayMillis();
                        }
                        try {
                            sender.sendDelayRetryableMessage(message, retryProps.getQueueEnum(), delayMillis, retryProps);
                        } catch (MqRetryException e) {
                            log.error("发送重试消息失败", e);
                        }
                        break;
                    case PROGRESSIVE:
                        delayMillis = retryProps.getDelays().get(0);
                        retryProps.getDelays().remove(0);
                        try {
                            sender.sendDelayRetryableMessage(message, retryProps.getQueueEnum(), delayMillis, retryProps);
                        } catch (MqRetryException e) {
                            log.error("发送重试消息失败", e);
                        }
                        break;
                    default:
                        log.warn("不支持的重试策略：{}", retryProps.getStrategy());
                }
            }
        } catch (CacheException e) {
            throw new MqRetryException(e);
        }
    }
}
