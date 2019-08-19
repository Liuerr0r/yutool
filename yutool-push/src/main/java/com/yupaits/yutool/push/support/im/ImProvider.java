package com.yupaits.yutool.push.support.im;


import com.yupaits.yutool.push.model.msg.ImMsg;

/**
 * 发送IM消息服务商接口
 * @author yupaits
 * @date 2019/7/27
 */
public interface ImProvider {

    /**
     * 发送IM消息
     * @param imMsg IM消息
     * @param to 消息接收者
     * @param content 消息文本内容
     */
    void sendImMsg(ImMsg imMsg, String to, String content);
}
