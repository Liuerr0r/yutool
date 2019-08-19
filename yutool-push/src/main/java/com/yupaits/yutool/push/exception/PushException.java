package com.yupaits.yutool.push.exception;

/**
 * 消息推送异常
 * @author yupaits
 * @date 2019/7/19
 */
public class PushException extends Exception {
    private static final long serialVersionUID = 6218950913055078008L;

    public PushException(String message) {
        super(message);
    }

    public PushException(String message, Throwable cause) {
        super(message, cause);
    }

    public PushException(Throwable cause) {
        super(cause);
    }
}
