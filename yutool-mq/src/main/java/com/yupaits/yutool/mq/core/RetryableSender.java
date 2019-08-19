package com.yupaits.yutool.mq.core;


import com.yupaits.yutool.mq.exception.MqRetryException;
import com.yupaits.yutool.mq.support.RetryProps;

import java.io.Serializable;

/**
 * 重试消息发送接口
 * @author yupaits
 * @date 2019/8/5
 */
public interface RetryableSender extends Sender {

    /**
     * 向指定重试队列发送消息
     * @param message 消息体
     * @param queueEnum 队列枚举
     * @param retryProps RetryProps参数
     * @throws MqRetryException 抛出MqRetryException
     */
    <M extends Serializable> void sendRetryableMessage(M message, IQueueEnum queueEnum, RetryProps retryProps) throws MqRetryException;

    /**
     * 向指定延迟重试队列发送消息
     * @param message 消息体
     * @param queueEnum 队列枚举
     * @param delayMillis 延迟毫秒数
     * @param retryProps RetryProps参数
     * @throws MqRetryException 抛出MqRetryException
     */
    <M extends Serializable> void sendDelayRetryableMessage(M message, IQueueEnum queueEnum, long delayMillis, RetryProps retryProps) throws MqRetryException;
}
