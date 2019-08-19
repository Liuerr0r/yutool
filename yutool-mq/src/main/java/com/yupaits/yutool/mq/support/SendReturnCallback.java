package com.yupaits.yutool.mq.support;

import com.yupaits.yutool.cache.core.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;

/**
 * 发送失败Return回调
 * @author yupaits
 * @date 2019/8/7
 */
@Deprecated
@Slf4j
public class SendReturnCallback implements RabbitTemplate.ReturnCallback {
    private final CacheTemplate cacheTemplate;

    public SendReturnCallback(CacheTemplate cacheTemplate) {
        this.cacheTemplate = cacheTemplate;
    }

    @Override
    public void returnedMessage(@NonNull Message message, @NonNull int replyCode, @NonNull String replyText,
                                @NonNull String exchange, @NonNull String routingKey) {
        //TODO 发送失败处理
        log.info("");
    }
}
