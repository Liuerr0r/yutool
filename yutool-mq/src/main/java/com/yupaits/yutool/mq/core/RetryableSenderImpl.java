package com.yupaits.yutool.mq.core;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.mq.exception.MqRetryException;
import com.yupaits.yutool.mq.support.MessageCacheKey;
import com.yupaits.yutool.mq.support.RetryProps;
import com.yupaits.yutool.mq.support.RetryPropsCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * 重试消息发送实现类
 * @author yupaits
 * @date 2019/8/5
 */
@Slf4j
public class RetryableSenderImpl extends SenderImpl implements RetryableSender {
    public static final String MESSAGE_KEY_HEADER = "messageKey";
    public static final String PROPS_KEY_HEADER = "propsKey";
    public static final String CACHE_PROPS_HEADER = "cacheProps";

    private final RabbitTemplate rabbitTemplate;
    private final CacheTemplate cacheTemplate;

    @Value("${id.generator.workerId:0}")
    private long workerId;
    @Value("${id.generator.datacenterId:0}")
    private long datacenterId;

    public RetryableSenderImpl(RabbitTemplate rabbitTemplate, CacheTemplate cacheTemplate) {
        super(rabbitTemplate);
        this.rabbitTemplate = rabbitTemplate;
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public <M extends Serializable> void sendRetryableMessage(M message, IQueueEnum queueEnum, RetryProps retryProps) throws MqRetryException {
        checkRetryProps(retryProps);
        retryProps.setQueueEnum(queueEnum);
        if (retryProps.getCacheProps() == null) {
            retryProps.setDefaultCacheProps();
        }
        if (StringUtils.isBlank(retryProps.getCorrelationId())) {
            retryProps.setCorrelationId(new Snowflake(workerId, datacenterId, true).nextIdStr());
        }
        String messageKey = new MessageCacheKey(retryProps.getCorrelationId()).cacheKey();
        String propsKey = new RetryPropsCacheKey(retryProps.getCorrelationId()).cacheKey();
        try {
            cacheTemplate.setCache(messageKey, message, retryProps.getCacheProps());
            cacheTemplate.setCache(propsKey, retryProps, retryProps.getCacheProps());
        } catch (CacheException e) {
            throw new AmqpException(e);
        }
        rabbitTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), message, msg -> {
            msg.getMessageProperties().setCorrelationId(retryProps.getCorrelationId());
            msg.getMessageProperties().setHeader(MESSAGE_KEY_HEADER, messageKey);
            msg.getMessageProperties().setHeader(PROPS_KEY_HEADER, propsKey);
            msg.getMessageProperties().setHeader(CACHE_PROPS_HEADER, JSON.toJSONString(retryProps.getCacheProps()));
            return msg;
        });
    }

    @Override
    public <M extends Serializable> void sendDelayRetryableMessage(M message, IQueueEnum queueEnum, long delayMillis, RetryProps retryProps) throws MqRetryException {
        checkRetryProps(retryProps);
        retryProps.setQueueEnum(queueEnum);
        if (retryProps.getCacheProps() == null) {
            retryProps.setDefaultCacheProps();
        }
        if (StringUtils.isBlank(retryProps.getCorrelationId())) {
            retryProps.setCorrelationId(new Snowflake(workerId, datacenterId, true).nextIdStr());
        }
        retryProps.getCacheProps().setTimeout(delayMillis + 10000L);
        if (delayMillis < 0L) {
            delayMillis = 0L;
        }
        final long delayMs = delayMillis;
        String messageKey = new MessageCacheKey(retryProps.getCorrelationId()).cacheKey();
        String propsKey = new RetryPropsCacheKey(retryProps.getCorrelationId()).cacheKey();
        try {
            cacheTemplate.setCache(messageKey, message, retryProps.getCacheProps());
            cacheTemplate.setCache(propsKey, retryProps, retryProps.getCacheProps());
        } catch (CacheException e) {
            throw new AmqpException(e);
        }
        rabbitTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), message, msg -> {
            msg.getMessageProperties().setCorrelationId(retryProps.getCorrelationId());
            msg.getMessageProperties().setHeader(MESSAGE_KEY_HEADER, messageKey);
            msg.getMessageProperties().setHeader(PROPS_KEY_HEADER, propsKey);
            msg.getMessageProperties().setHeader(CACHE_PROPS_HEADER, JSON.toJSONString(retryProps.getCacheProps()));
            //给消息设置延迟毫秒值
            msg.getMessageProperties().setExpiration(String.valueOf(delayMs));
            return msg;
        });
    }
}
