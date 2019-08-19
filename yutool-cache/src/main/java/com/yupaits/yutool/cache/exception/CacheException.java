package com.yupaits.yutool.cache.exception;

/**
 * 缓存相关操作异常
 * @author yupaits
 * @date 2019/7/16
 */
public class CacheException extends Exception {
    private static final long serialVersionUID = 757656148764760559L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
