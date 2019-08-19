package com.yupaits.yutool.push.handler;


import com.yupaits.yutool.push.exception.HandleReplyException;
import com.yupaits.yutool.push.model.reply.IReply;

/**
 * 回复消息处理接口
 * @author yupaits
 * @date 2019/7/19
 */
public interface ReplyHandler<R extends IReply> {

    /**
     * 回复消息处理
     * @param reply 回复消息体
     * @throws HandleReplyException 抛出HandleReplyException
     */
    void handleReply(R reply) throws HandleReplyException;
}
