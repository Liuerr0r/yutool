package com.yupaits.yutool.push.receiver;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.ImMsg;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.push.support.im.ImProvider;
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
 * IM消息监听器
 * @author yupaits
 * @date 2019/7/19
 */
@Slf4j
@Component
@RabbitListener(queues = "push.im")
public class ImReceiver extends BasePushReceiver {
    private final ImProvider imProvider;

    @Autowired
    protected ImReceiver(RetryableSender sender, CacheTemplate cacheTemplate, ImProvider imProvider) {
        super(sender, cacheTemplate);
        this.imProvider = imProvider;
    }

    @Override
    protected void push(MsgPayload payload) throws PushException {
        ImMsg imMsg;
        if (payload.getMsg() instanceof ImMsg) {
            imMsg = (ImMsg) payload.getMsg();
        } else {
            imMsg = payload.getMsg().toMsg(ImMsg.class);
        }
        if (imMsg == null || !imMsg.isValid()) {
            throw new PushException(String.format("IM消息校验失败，参数: %s", this));
        }
        try {
            String content = TemplateUtils.processToString(imMsg.getMsgTemplate().getTemplateDir(),
                    imMsg.getMsgTemplate().getTemplateFilename(), imMsg.getParams());
            batchSendImMsg(imMsg, payload.getPushProps().getReceivers().getOrDefault(PushType.IM, Lists.newArrayList()), content);
        } catch (Exception e) {
            throw new PushException("发送IM消息操作失败", e);
        }
    }

    /**
     * 批量发送IM消息
     * @param imMsg IM消息
     * @param receivers 消息接收者集合
     */
    private void batchSendImMsg(ImMsg imMsg, Collection<String> receivers, String content) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("immsg-worker-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (String receiver : receivers) {
            pool.execute(() -> imProvider.sendImMsg(imMsg, receiver, content));
        }
        pool.shutdown();
    }

    @RabbitHandler
    @Override
    public void handle(@Payload MsgPayload payload, @Headers Map<String, Object> headers) {
        if (payload == null || !payload.isValid()) {
            log.error("IM消息校验失败，参数: {}", payload);
            return;
        }
        try {
            push(payload);
        } catch (PushException e) {
            log.error("IM消息推送失败", e);
        }
    }
}
