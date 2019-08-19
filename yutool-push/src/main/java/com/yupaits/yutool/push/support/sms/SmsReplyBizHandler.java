package com.yupaits.yutool.push.support.sms;


import com.yupaits.yutool.push.model.reply.SmsReply;

/**
 * 短信回复业务处理接口
 * @author yupaits
 * @date 2019/7/25
 */
public interface SmsReplyBizHandler {

    /**
     * 处理短信回复相关业务
     * @param smsReply 短信回复信息
     */
    void handleSmsReply(SmsReply smsReply);
}
