package com.yupaits.yutool.mq.core;

import java.io.Serializable;

/**
 * 消息发送接口
 * @author yupaits
 * @date 2019/7/18
 */
public interface Sender {

    /**
     * 向指定队列发送消息
     * @param message 消息体
     * @param queueEnum 队列枚举
     */
    <M extends Serializable> void sendMessage(M message, IQueueEnum queueEnum);

    /**
     * 向指定延迟队列发送消息
     * @param message 消息体
     * @param queueEnum 队列枚举
     * @param delayMillis 延迟毫秒数
     */
    <M extends Serializable> void sendDelayMessage(M message, IQueueEnum queueEnum, long delayMillis);
}
