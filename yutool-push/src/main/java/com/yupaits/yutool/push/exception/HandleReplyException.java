package com.yupaits.yutool.push.exception;

/**
 * 回复消息处理异常
 * @author yupaits
 * @date 2019/7/19
 */
public class HandleReplyException extends Exception {
    private static final long serialVersionUID = -71362621283372311L;

    public HandleReplyException(String message) {
        super(message);
    }

    public HandleReplyException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleReplyException(Throwable cause) {
        super(cause);
    }
}
