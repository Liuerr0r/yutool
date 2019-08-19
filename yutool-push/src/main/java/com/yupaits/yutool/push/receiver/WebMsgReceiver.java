package com.yupaits.yutool.push.receiver;

import com.gexin.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.WebMsg;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.push.support.webmsg.WebMessage;
import com.yupaits.yutool.push.support.webmsg.WebMsgHandler;
import com.yupaits.yutool.template.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Web网页消息监听器
 * @author yupaits
 * @date 2019/7/19
 */
@Slf4j
@Component
@RabbitListener(queues = "push.webmsg")
public class WebMsgReceiver extends BasePushReceiver {
    private final WebMsgHandler webMsgHandler;

    @Autowired
    protected WebMsgReceiver(RetryableSender sender, CacheTemplate cacheTemplate, WebMsgHandler webMsgHandler) {
        super(sender, cacheTemplate);
        this.webMsgHandler = webMsgHandler;
    }

    @Override
    protected void push(MsgPayload payload) throws PushException {
        WebMsg webMsg;
        if (payload.getMsg() instanceof WebMsg) {
            webMsg = (WebMsg) payload.getMsg();
        } else {
            webMsg = payload.getMsg().toMsg(WebMsg.class);
        }
        if (webMsg == null || !webMsg.isValid()) {
            throw new PushException(String.format("Web网页消息校验失败，参数：%s", this));
        }
        try {
            String content = TemplateUtils.processToString(webMsg.getMsgTemplate().getTemplateDir(),
                    webMsg.getMsgTemplate().getTemplateFilename(), webMsg.getParams());
            WebMessage message = WebMessage.builder()
                    .action(webMsg.getAction())
                    .title(webMsg.getTitle())
                    .content(content)
                    .extras(webMsg.getExtras())
                    .build();
            batchSendWebMsg(payload.getPushProps().getReceivers().getOrDefault(PushType.WEB_MSG, Lists.newArrayList()), new TextMessage(JSON.toJSONString(message)));
        } catch (Exception e) {
            throw new PushException("推送Web网页消息操作失败", e);
        }
    }

    /**
     * 批量推送Web网页消息
     * @param userIds 目标用户ID集合
     * @param message 推送的消息
     */
    private void batchSendWebMsg(Collection<String> userIds, TextMessage message) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("webmsg-worker-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (String userId : userIds) {
            pool.execute(() -> webMsgHandler.sendMessageToUser(userId, message));
        }
        pool.shutdown();
    }

    @RabbitHandler
    @Override
    public void handle(@Payload MsgPayload payload, @Headers Map<String, Object> headers) {
        if (payload == null || !payload.isValid()) {
            log.error("Web网页消息校验失败，参数: {}", payload);
            return;
        }
        try {
            push(payload);
        } catch (PushException e) {
            log.error("Web网页消息推送失败", e);
        }
    }
}
