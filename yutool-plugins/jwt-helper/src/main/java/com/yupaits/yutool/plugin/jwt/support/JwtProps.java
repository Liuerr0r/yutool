package com.yupaits.yutool.plugin.jwt.support;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT配置
 * @author yupaits
 * @date 2019/8/21
 */
@Data
@ConfigurationProperties("jwt")
public class JwtProps {
    /**
     * Token密钥，默认随机生成
     */
    private String secret = RandomStringUtils.randomAlphanumeric(12);

    /**
     * 有效期，单位秒，默认是2周
     */
    private int expiredIn = 1209600;

    /**
     * JWT Header名称，默认是 Authorization
     */
    private String authHeader = "Authorization";
}
