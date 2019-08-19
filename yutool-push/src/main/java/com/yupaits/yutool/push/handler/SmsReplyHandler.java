package com.yupaits.yutool.push.handler;


import com.yupaits.yutool.push.exception.HandleReplyException;
import com.yupaits.yutool.push.model.reply.SmsReply;
import com.yupaits.yutool.push.support.sms.SmsReplyBizHandler;
import com.yupaits.yutool.push.support.sms.SmsReplyStore;

/**
 * 短信回复处理器
 * @author yupaits
 * @date 2019/8/10
 */
public class SmsReplyHandler implements ReplyHandler<SmsReply> {
    private final SmsReplyStore smsReplyStore;
    private final SmsReplyBizHandler smsReplyBizHandler;

    public SmsReplyHandler(SmsReplyStore smsReplyStore, SmsReplyBizHandler smsReplyBizHandler) {
        this.smsReplyStore = smsReplyStore;
        this.smsReplyBizHandler = smsReplyBizHandler;
    }

    @Override
    public void handleReply(SmsReply reply) throws HandleReplyException {
        if (reply == null || !reply.isValid()) {
            throw new HandleReplyException(String.format("短信消息回复校验失败，参数: %s", reply));
        }
        //记录短信回复信息
        smsReplyStore.save(reply);
        //调用业务处理接口
        smsReplyBizHandler.handleSmsReply(reply);
    }
}
