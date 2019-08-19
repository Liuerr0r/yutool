package com.yupaits.yutool.file.exception;

/**
 * 文件下载异常
 * @author yupaits
 * @date 2019/7/23
 */
public class DownloadException extends Exception {
    private static final long serialVersionUID = 2010900931979747608L;

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }
}
