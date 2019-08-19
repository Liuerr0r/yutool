package com.yupaits.yutool.mq.core;

import com.yupaits.yutool.mq.exception.MqRetryException;
import com.yupaits.yutool.mq.support.RetryProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

import java.io.Serializable;

/**
 * 消息发送实现类
 * @author yupaits
 * @date 2019/7/18
 */
@Slf4j
public class SenderImpl implements Sender {
    private final AmqpTemplate amqpTemplate;

    public SenderImpl(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public <M extends Serializable> void sendMessage(M message, IQueueEnum queueEnum) {
        amqpTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), message);
    }

    @Override
    public <M extends Serializable> void sendDelayMessage(M message, IQueueEnum queueEnum, long delayMillis) {
        if (delayMillis < 0L) {
            delayMillis = 0L;
        }
        final long delayMs = delayMillis;
        amqpTemplate.convertAndSend(queueEnum.getExchange(), queueEnum.getRouteKey(), message, msg -> {
            //给消息设置延迟毫秒值
            msg.getMessageProperties().setExpiration(String.valueOf(delayMs));
            return msg;
        });
    }

    /**
     * 校验RetryProps参数
     * @param retryProps RetryProps参数
     * @throws MqRetryException 抛出MqRetryException
     */
    protected void checkRetryProps(RetryProps retryProps) throws MqRetryException {
        if (retryProps == null || !retryProps.isValid()) {
            throw new MqRetryException(String.format("重试队列配置校验失败，配置信息：%s", retryProps));
        }
    }
}
