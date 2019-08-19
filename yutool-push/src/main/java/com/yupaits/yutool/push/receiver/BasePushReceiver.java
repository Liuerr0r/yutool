package com.yupaits.yutool.push.receiver;

import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.mq.core.Receiver;
import com.yupaits.yutool.mq.core.RetryableSender;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.MsgPayload;

/**
 * 推送消息监听器
 * @author yupaits
 * @date 2019/8/10
 */
public abstract class BasePushReceiver extends Receiver<MsgPayload> {

    protected BasePushReceiver(RetryableSender sender, CacheTemplate cacheTemplate) {
        super(sender, cacheTemplate);
    }

    /**
     * 消息推送
     * @param payload 消息Payload
     * @throws PushException 抛出PushException
     */
    protected abstract void push(MsgPayload payload) throws PushException;
}
