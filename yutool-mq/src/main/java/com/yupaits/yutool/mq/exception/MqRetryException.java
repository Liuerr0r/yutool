package com.yupaits.yutool.mq.exception;

/**
 * 消息重试异常
 * @author yupaits
 * @date 2019/7/19
 */
public class MqRetryException extends Exception {
    private static final long serialVersionUID = 2576420548179819723L;

    public MqRetryException(String message) {
        super(message);
    }

    public MqRetryException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqRetryException(Throwable cause) {
        super(cause);
    }
}
