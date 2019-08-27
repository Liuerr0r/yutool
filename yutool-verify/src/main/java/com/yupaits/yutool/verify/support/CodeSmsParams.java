package com.yupaits.yutool.verify.support;

import java.util.HashMap;

/**
 * 验证码短信模板参数
 * @author yupaits
 * @date 2019/8/27
 */
public class CodeSmsParams extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static final String CODE_KEY = "code";

    /**
     * 设置验证码参数
     * @param code 验证码
     */
    public void setCode(String code) {
        this.put(CODE_KEY, code);
    }
}
