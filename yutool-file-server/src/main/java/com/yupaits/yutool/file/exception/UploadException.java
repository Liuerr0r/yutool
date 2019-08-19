package com.yupaits.yutool.file.exception;

/**
 * 上传文件异常
 * @author yupaits
 * @date 2019/7/22
 */
public class UploadException extends Exception {
    private static final long serialVersionUID = 3282598683402807034L;

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadException(Throwable cause) {
        super(cause);
    }
}
