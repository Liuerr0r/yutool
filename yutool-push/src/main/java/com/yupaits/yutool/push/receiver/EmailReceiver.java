package com.yupaits.yutool.push.receiver;

import com.google.common.collect.Lists;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.EmailMsg;
import com.yupaits.yutool.push.support.PushType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * 邮件消息监听器
 * @author yupaits
 * @date 2019/7/19
 */
@Slf4j
@Component
@RabbitListener(queues = "push.email")
public class EmailReceiver extends BasePushReceiver {
    private final JavaMailSender javaMailSender;

    @Autowired
    protected EmailReceiver(RetryableSender sender, CacheTemplate cacheTemplate, JavaMailSender javaMailSender) {
        super(sender, cacheTemplate);
        this.javaMailSender = javaMailSender;
    }

    @Override
    protected void push(MsgPayload payload) throws PushException {
        EmailMsg emailMsg;
        if (payload.getMsg() instanceof EmailMsg) {
            emailMsg = (EmailMsg) payload.getMsg();
        } else {
            emailMsg = payload.getMsg().toMsg(EmailMsg.class);
        }
        if (emailMsg == null || !emailMsg.isValid()) {
            throw new PushException(String.format("邮件消息校验失败，参数: %s", this));
        }
        try {
            javaMailSender.send(emailMsg.from(payload.getPushProps()));
            log.info("发送邮件成功，From：{}，To：{}，主题：{}", emailMsg.getFrom(),
                    Arrays.toString(payload.getPushProps().getReceivers().getOrDefault(PushType.EMAIL, Lists.newArrayList()).toArray()),
                    emailMsg.getSubject());
        } catch (Exception e) {
            throw new PushException(String.format("发送邮件操作失败，From：%s，To：%s，主题：%s", emailMsg.getFrom(),
                    Arrays.toString(payload.getPushProps().getReceivers().getOrDefault(PushType.EMAIL, Lists.newArrayList()).toArray()),
                    emailMsg.getSubject()), e);
        }
    }

    @RabbitHandler
    @Override
    public void handle(@Payload MsgPayload payload, @Headers Map<String, Object> headers) {
        if (payload == null || !payload.isValid()) {
            log.error("邮件消息校验失败，参数: {}", payload);
            return;
        }
        try {
            push(payload);
        } catch (PushException e) {
            log.error("邮件消息推送失败", e);
        }
    }
}
