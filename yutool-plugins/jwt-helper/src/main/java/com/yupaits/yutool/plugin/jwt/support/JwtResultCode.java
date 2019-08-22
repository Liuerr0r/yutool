package com.yupaits.yutool.plugin.jwt.support;

import com.yupaits.yutool.commons.result.IResultCode;
import lombok.Getter;

/**
 * Jwt响应枚举
 * @author yupaits
 * @date 2019/8/22
 */
@Getter
public enum JwtResultCode implements IResultCode {
    /**
     * 无效的token
     */
    TOKEN_INVALID(30002, "无效的token"),
    TOKEN_ILLEGAL(30003, "token不合法");

    private int code;
    private String message;

    JwtResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
