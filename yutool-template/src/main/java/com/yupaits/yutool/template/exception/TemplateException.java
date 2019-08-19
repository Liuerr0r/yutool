package com.yupaits.yutool.template.exception;

/**
 * 模板相关异常
 * @author yupaits
 * @date 2019/7/22
 */
public class TemplateException extends Exception {
    private static final long serialVersionUID = 8648880315416194321L;

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }
}
