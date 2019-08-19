package com.yupaits.yutool.mq.config;

import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.mq.core.RetryableSenderImpl;
import com.yupaits.yutool.mq.core.Sender;
import com.yupaits.yutool.mq.core.SenderImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列自动装配
 * @author yupaits
 * @date 2019/7/18
 */
@Configuration
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class MqAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CacheTemplate.class)
    public RetryableSender retryableSender(RabbitTemplate rabbitTemplate, CacheTemplate cacheTemplate) {
        return new RetryableSenderImpl(rabbitTemplate, cacheTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(RetryableSender.class)
    public Sender sender(RabbitTemplate rabbitTemplate) {
        return new SenderImpl(rabbitTemplate);
    }
}
