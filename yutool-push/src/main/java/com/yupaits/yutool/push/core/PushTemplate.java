package com.yupaits.yutool.push.core;


import com.yupaits.yutool.mq.core.Sender;
import com.yupaits.yutool.push.exception.HandleReplyException;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.handler.SmsReplyHandler;
import com.yupaits.yutool.push.model.MsgPayload;
import com.yupaits.yutool.push.model.msg.IMsg;
import com.yupaits.yutool.push.model.reply.IReply;
import com.yupaits.yutool.push.model.reply.SmsReply;
import com.yupaits.yutool.push.support.PushProps;
import com.yupaits.yutool.push.support.PushQueue;
import com.yupaits.yutool.push.support.PushType;

import java.util.Set;

/**
 * 消息推送
 * @author yupaits
 * @date 2019/7/19
 */
public class PushTemplate {

    private final Sender sender;
    private SmsReplyHandler smsReplyHandler;

    public PushTemplate(Sender sender) {
        this.sender = sender;
    }

    public PushTemplate(Sender sender, SmsReplyHandler smsReplyHandler) {
        this.sender = sender;
        this.smsReplyHandler = smsReplyHandler;
    }

    /**
     * 统一推送接口，含自定义PushProps参数
     * @param msg 推送消息体
     * @param pushProps PushProps参数
     * @throws PushException 抛出PushException
     */
    public void push(IMsg msg, PushProps pushProps) throws PushException {
        checkPushProps(pushProps);
        MsgPayload msgPayload = new MsgPayload(msg, pushProps);
        Set<PushType> pushTypes = pushProps.getReceivers().keySet();
        if (pushProps.isDelayed()) {
            long delayMillis = pushProps.getDelayMillis();
            if (pushTypes.contains(PushType.NOTIFICATION)) {
                sender.sendDelayMessage(msgPayload, PushQueue.NOTIFICATION_TTL, delayMillis);
            }
            if (pushTypes.contains(PushType.WEB_MSG)) {
                sender.sendDelayMessage(msgPayload, PushQueue.WEB_MSG_TTL, delayMillis);
            }
            if (pushTypes.contains(PushType.SMS)) {
                sender.sendDelayMessage(msgPayload, PushQueue.SMS_TTL, delayMillis);
            }
            if (pushTypes.contains(PushType.EMAIL)) {
                sender.sendDelayMessage(msgPayload, PushQueue.EMAIL_TTL, delayMillis);
            }
            if (pushTypes.contains(PushType.IM)) {
                sender.sendDelayMessage(msgPayload, PushQueue.IM_TTL, delayMillis);
            }
        } else {
            if (pushTypes.contains(PushType.NOTIFICATION)) {
                sender.sendMessage(msgPayload, PushQueue.NOTIFICATION);
            }
            if (pushTypes.contains(PushType.WEB_MSG)) {
                sender.sendMessage(msgPayload, PushQueue.WEB_MSG);
            }
            if (pushTypes.contains(PushType.SMS)) {
                sender.sendMessage(msgPayload, PushQueue.SMS);
            }
            if (pushTypes.contains(PushType.EMAIL)) {
                sender.sendMessage(msgPayload, PushQueue.EMAIL);
            }
            if (pushTypes.contains(PushType.IM)) {
                sender.sendMessage(msgPayload, PushQueue.IM);
            }
        }
    }

    /**
     * 统一回复处理接口
     * @param reply 消息回复
     * @throws HandleReplyException 抛出HandleReplyException
     */
    public void onReply(IReply reply) throws HandleReplyException {
        if (reply instanceof SmsReply && this.smsReplyHandler != null) {
            smsReplyHandler.handleReply((SmsReply) reply);
        }
    }

    /**
     * 校验PushProps参数
     * @param pushProps PushProps参数
     * @throws PushException 抛出PushException
     */
    private void checkPushProps(PushProps pushProps) throws PushException {
        if (pushProps == null || !pushProps.isValid()) {
            throw new PushException(String.format("推送配置校验失败，配置信息：%s", pushProps));
        }
    }
}
