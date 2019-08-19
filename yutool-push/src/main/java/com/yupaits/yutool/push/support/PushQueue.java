package com.yupaits.yutool.push.support;

import com.yupaits.yutool.mq.core.IQueueEnum;
import lombok.Getter;

/**
 * 推送消息队列枚举
 * @author yupaits
 * @date 2019/7/19
 */
@Getter
public enum PushQueue implements IQueueEnum {
    /**
     * APP通知
     */
    NOTIFICATION("push.notification.direct", "push.notification", "push.notification"),
    /**
     * Web网页消息
     */
    WEB_MSG("push.webmsg.direct", "push.webmsg", "push.webmsg"),
    /**
     * 短信
     */
    SMS("push.sms.direct", "push.sms", "push.sms"),
    /**
     * 邮件
     */
    EMAIL("push.email.direct", "push.email", "push.email"),
    /**
     * IM消息
     */
    IM("push.im.direct", "push.im", "push.im"),
    /**
     * APP通知延迟队列
     */
    NOTIFICATION_TTL("push.notification.direct.ttl", "push.notification.ttl", "push.notification.ttl"),
    /**
     * Web网页消息延迟队列
     */
    WEB_MSG_TTL("push.webmsg.direct.ttl", "push.webmsg.ttl", "push.webmsg.ttl"),
    /**
     * 短信延迟队列
     */
    SMS_TTL("push.sms.direct.ttl", "push.sms.ttl", "push.sms.ttl"),
    /**
     * 邮件延迟队列
     */
    EMAIL_TTL("push.email.direct.ttl", "push.email.ttl", "push.email.ttl"),
    /**
     * IM消息延迟队列
     */
    IM_TTL("push.im.direct.ttl", "push.im.ttl", "push.im.ttl"),
    ;

    private String exchange;
    private String name;
    private String routeKey;

    PushQueue(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
