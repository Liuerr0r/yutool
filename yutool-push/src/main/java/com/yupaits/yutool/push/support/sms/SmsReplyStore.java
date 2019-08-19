package com.yupaits.yutool.push.support.sms;


import com.yupaits.yutool.push.model.reply.SmsReply;

/**
 * 短信回复记录存储接口
 * @author yupaits
 * @date 2019/7/25
 */
public interface SmsReplyStore {

    /**
     * 保存短信回复记录
     * @param smsReply 短信回复信息
     */
    void save(SmsReply smsReply);
}
