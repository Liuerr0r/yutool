package com.yupaits.yutool.mq.utils;

import com.yupaits.yutool.mq.core.IQueueEnum;
import org.springframework.amqp.core.*;

/**
 * 消息队列工具
 * @author yupaits
 * @date 2019/7/18
 */
public class MqUtils {
    private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 根据QueueEnum生成交换机
     * @param queueEnum 队列枚举
     * @return 交换机
     */
    public static DirectExchange direct(IQueueEnum queueEnum) {
        return (DirectExchange) ExchangeBuilder.directExchange(queueEnum.getExchange()).durable(true).build();
    }

    /**
     * 根据QueueEnum生成队列
     * @param queueEnum 队列枚举
     * @return 队列
     */
    public static Queue queue(IQueueEnum queueEnum) {
        return QueueBuilder.durable(queueEnum.getName()).autoDelete().build();
    }

    /**
     * 根据QueueEnum生成延迟队列
     * @param queueEnum 队列枚举
     * @param forwardQueueEnum 转发队列
     * @return 延迟队列
     */
    public static Queue ttlQueue(IQueueEnum queueEnum, IQueueEnum forwardQueueEnum) {
        return QueueBuilder.durable(queueEnum.getName())
                .withArgument(X_DEAD_LETTER_EXCHANGE, forwardQueueEnum.getExchange())
                .withArgument(X_DEAD_LETTER_ROUTING_KEY, forwardQueueEnum.getRouteKey())
                .autoDelete()
                .build();
    }

    /**
     * 将队列绑定到交换机
     * @param exchange 交换机
     * @param queue 队列
     * @return 绑定关系
     */
    public static Binding binding(DirectExchange exchange, Queue queue, IQueueEnum queueEnum) {
        return BindingBuilder.bind(queue).to(exchange).with(queueEnum.getRouteKey());
    }
}
