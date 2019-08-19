package com.yupaits.yutool.push.receiver;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.SmsMsg;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.push.support.sms.SmsProvider;
import com.yupaits.yutool.template.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 短信消息监听器
 * @author yupaits
 * @date 2019/7/19
 */
@Slf4j
@Component
@RabbitListener(queues = "push.sms")
public class SmsReceiver extends BasePushReceiver {
    private final SmsProvider smsProvider;

    @Autowired
    protected SmsReceiver(RetryableSender sender, CacheTemplate cacheTemplate, SmsProvider smsProvider) {
        super(sender, cacheTemplate);
        this.smsProvider = smsProvider;
    }

    @Override
    protected void push(MsgPayload payload) throws PushException {
        SmsMsg smsMsg;
        if (payload.getMsg() instanceof SmsMsg) {
            smsMsg = (SmsMsg) payload.getMsg();
        } else {
            smsMsg = payload.getMsg().toMsg(SmsMsg.class);
        }
        if (smsMsg == null || !smsMsg.isValid()) {
            throw new PushException(String.format("短信消息校验失败，参数: %s", this));
        }
        try {
            String content = TemplateUtils.processToString(smsMsg.getMsgTemplate().getTemplateDir(),
                    smsMsg.getMsgTemplate().getTemplateFilename(), smsMsg.getParams());
            batchSendSms(payload.getPushProps().getReceivers().getOrDefault(PushType.SMS, Lists.newArrayList()), content);
        } catch (Exception e) {
            throw new PushException("发送短信操作失败", e);
        }
    }

    /**
     * 批量发送短信
     * @param phoneNumbers 短信接收号码集合
     * @param content 短信内容
     */
    private void batchSendSms(Collection<String> phoneNumbers, String content) {
        //使用线程池和多线程批量发送短信
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("sms-worker-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (String phoneNumber : phoneNumbers) {
            pool.execute(() -> smsProvider.sendSms(phoneNumber, content));
        }
        pool.shutdown();
    }

    @RabbitHandler
    @Override
    public void handle(@Payload MsgPayload payload, @Headers Map<String, Object> headers) {
        if (payload == null || !payload.isValid()) {
            log.error("短信消息校验失败，参数: {}", payload);
            return;
        }
        try {
            push(payload);
        } catch (PushException e) {
            log.error("短信消息推送失败", e);
        }
    }
}
