package com.yupaits.yutool.verify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 验证码短信配置
 * @author yupaits
 * @date 2019/8/27
 */
@Data
@ConfigurationProperties(prefix = "sms.code", ignoreInvalidFields = true)
public class CodeSmsProps implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数字验证码位数
     */
    private int digits = 6;

    /**
     * 缓存过期时间分钟数，默认10分钟
     */
    private long timeout = 10;
}
