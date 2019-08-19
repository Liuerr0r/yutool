package com.yupaits.yutool.push.support.sms;

/**
 * 发送短信服务商接口
 * @author yupaits
 * @date 2019/7/22
 */
public interface SmsProvider {

    /**
     * 发送短信
     * @param phoneNumber 手机号码
     * @param content 短信内容
     */
    void sendSms(String phoneNumber, String content);
}
