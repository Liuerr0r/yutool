package com.yupaits.yutool.mq.support;

import com.yupaits.yutool.cache.core.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 消息确认回调
 * @author yupaits
 * @date 2019/8/7
 */
@Deprecated
@Slf4j
public class SendConfirmCallback implements RabbitTemplate.ConfirmCallback {
    private final CacheTemplate cacheTemplate;

    public SendConfirmCallback(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //TODO 消息确认处理
        if (ack) {
            log.info("消息确认成功，消息ID：{}", correlationData.getId());
            Message message = correlationData.getReturnedMessage();
            /*try {
                cacheTemplate.removeCache(messageKey, cacheProps);
                cacheTemplate.removeCache(propsKey, cacheProps);
            } catch (CacheException e) {
                throw new AmqpException(e);
            }*/
        } else {
            //TODO 失败处理
            log.warn("消息确认失败，原因：{}", cause);
        }
    }
}
