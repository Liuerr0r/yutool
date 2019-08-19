package com.yupaits.yutool.push.receiver;

import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.Notification;
import com.yupaits.yutool.push.support.notification.NotificationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * APP通知消息监听器
 * @author yupaits
 * @date 2019/7/19
 */
@Slf4j
@Component
@RabbitListener(queues = "push.notification")
public class NotificationReceiver extends BasePushReceiver {
    private final NotificationProvider notificationProvider;

    @Autowired
    protected NotificationReceiver(RetryableSender sender, CacheTemplate cacheTemplate, NotificationProvider notificationProvider) {
        super(sender, cacheTemplate);
        this.notificationProvider = notificationProvider;
    }

    @Override
    protected void push(MsgPayload payload) throws PushException {
        Notification notification;
        if (payload.getMsg() instanceof Notification) {
            notification = (Notification) payload.getMsg();
        } else {
            notification = payload.getMsg().toMsg(Notification.class);
        }
        if (notification == null || !notification.isValid()) {
            throw new PushException(String.format("APP通知校验失败，参数：%s", this));
        }
        try {
            notificationProvider.pushNotification(notification, payload.getPushProps());
        } catch (Exception e) {
            throw new PushException("推送APP通知操作失败", e);
        }
    }

    @RabbitHandler
    @Override
    public void handle(@Payload MsgPayload payload, @Headers Map<String, Object> headers) {
        if (payload == null || !payload.isValid()) {
            log.error("APP通知消息校验失败，参数: {}", payload);
            return;
        }
        try {
            push(payload);
        } catch (PushException e) {
            log.error("APP通知消息推送失败", e);
        }
    }
}
