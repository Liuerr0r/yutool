package com.yupaits.yutool.push.config;

import com.yupaits.yutool.mq.core.Sender;
import com.yupaits.yutool.mq.utils.MqUtils;
import com.yupaits.yutool.push.core.PushTemplate;
import com.yupaits.yutool.push.handler.SmsReplyHandler;
import com.yupaits.yutool.push.receiver.*;
import com.yupaits.yutool.push.support.PushQueue;
import com.yupaits.yutool.push.support.sms.SmsReplyBizHandler;
import com.yupaits.yutool.push.support.sms.SmsReplyStore;
import com.yupaits.yutool.push.support.webmsg.WebMsgHandler;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 推送服务自动装配
 * @author yupaits
 * @date 2019/7/19
 */
@Configuration
@ComponentScan(basePackageClasses = {EmailReceiver.class, ImReceiver.class, NotificationReceiver.class,
        SmsReceiver.class, WebMsgReceiver.class, WebMsgHandler.class})
public class PushAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({SmsReplyStore.class, SmsReplyBizHandler.class})
    public SmsReplyHandler smsReplyHandler(SmsReplyStore smsReplyStore, SmsReplyBizHandler smsReplyBizHandler) {
        return new SmsReplyHandler(smsReplyStore, smsReplyBizHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(SmsReplyHandler.class)
    public PushTemplate pushTemplate(Sender sender, SmsReplyHandler smsReplyHandler) {
        return new PushTemplate(sender, smsReplyHandler);
    }

    @Bean
    @ConditionalOnMissingBean(SmsReplyHandler.class)
    public PushTemplate pushTemplate(Sender sender) {
        return new PushTemplate(sender);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return MqUtils.direct(PushQueue.NOTIFICATION);
    }

    @Bean
    public DirectExchange webMsgExchange() {
        return MqUtils.direct(PushQueue.WEB_MSG);
    }

    @Bean
    public DirectExchange smsExchange() {
        return MqUtils.direct(PushQueue.SMS);
    }

    @Bean
    public DirectExchange emailExchange() {
        return MqUtils.direct(PushQueue.EMAIL);
    }

    @Bean
    public DirectExchange imExchange() {
        return MqUtils.direct(PushQueue.IM);
    }

    @Bean
    public DirectExchange notificationTtlExchange() {
        return MqUtils.direct(PushQueue.NOTIFICATION_TTL);
    }

    @Bean
    public DirectExchange webMsgTtlExchange() {
        return MqUtils.direct(PushQueue.WEB_MSG_TTL);
    }

    @Bean
    public DirectExchange smsTtlExchange() {
        return MqUtils.direct(PushQueue.SMS_TTL);
    }

    @Bean
    public DirectExchange emailTtlExchange() {
        return MqUtils.direct(PushQueue.EMAIL_TTL);
    }

    @Bean
    public DirectExchange imTtlExchange() {
        return MqUtils.direct(PushQueue.IM_TTL);
    }

    @Bean
    public Queue notificationQueue() {
        return MqUtils.queue(PushQueue.NOTIFICATION);
    }

    @Bean
    public Queue webMsgQueue() {
        return MqUtils.queue(PushQueue.WEB_MSG);
    }

    @Bean
    public Queue smsQueue() {
        return MqUtils.queue(PushQueue.SMS);
    }

    @Bean
    public Queue emailQueue() {
        return MqUtils.queue(PushQueue.EMAIL);
    }

    @Bean
    public Queue imQueue() {
        return MqUtils.queue(PushQueue.IM);
    }

    @Bean
    public Queue notificationTtlQueue() {
        return MqUtils.ttlQueue(PushQueue.NOTIFICATION_TTL, PushQueue.NOTIFICATION);
    }

    @Bean
    public Queue webMsgTtlQueue() {
        return MqUtils.ttlQueue(PushQueue.WEB_MSG_TTL, PushQueue.WEB_MSG);
    }

    @Bean
    public Queue smsTtlQueue() {
        return MqUtils.ttlQueue(PushQueue.SMS_TTL, PushQueue.SMS);
    }

    @Bean
    public Queue emailTtlQueue() {
        return MqUtils.ttlQueue(PushQueue.EMAIL_TTL, PushQueue.EMAIL);
    }

    @Bean
    public Queue imTtlQueue() {
        return MqUtils.ttlQueue(PushQueue.IM_TTL, PushQueue.IM);
    }

    @Bean
    public Binding notificationBinding() {
        return MqUtils.binding(notificationExchange(), notificationQueue(), PushQueue.NOTIFICATION);
    }

    @Bean
    public Binding webMsgBinding() {
        return MqUtils.binding(webMsgExchange(), webMsgQueue(), PushQueue.WEB_MSG);
    }

    @Bean
    public Binding smsBinding() {
        return MqUtils.binding(smsExchange(), smsQueue(), PushQueue.SMS);
    }

    @Bean
    public Binding emailBinding() {
        return MqUtils.binding(emailExchange(), emailQueue(), PushQueue.EMAIL);
    }

    @Bean
    public Binding imBinding() {
        return MqUtils.binding(imExchange(), imQueue(), PushQueue.IM);
    }

    @Bean
    public Binding notificationTtlBinding() {
        return MqUtils.binding(notificationTtlExchange(), notificationTtlQueue(), PushQueue.NOTIFICATION_TTL);
    }

    @Bean
    public Binding webMsgTtlBinding() {
        return MqUtils.binding(webMsgTtlExchange(), webMsgTtlQueue(), PushQueue.WEB_MSG_TTL);
    }

    @Bean
    public Binding smsTtlBinding() {
        return MqUtils.binding(smsTtlExchange(), smsTtlQueue(), PushQueue.SMS_TTL);
    }

    @Bean
    public Binding emailTtlBinding() {
        return MqUtils.binding(emailTtlExchange(), emailTtlQueue(), PushQueue.EMAIL_TTL);
    }

    @Bean
    public Binding imTtlBinding() {
        return MqUtils.binding(imTtlExchange(), imTtlQueue(), PushQueue.IM_TTL);
    }
}
