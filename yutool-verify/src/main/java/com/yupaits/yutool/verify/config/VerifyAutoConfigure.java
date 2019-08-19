package com.yupaits.yutool.verify.config;

import com.yupaits.yutool.verify.core.VerifyTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码服务自动装配
 * @author yupaits
 * @date 2019/8/19
 */
@Configuration
public class VerifyAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public VerifyTemplate verifyTemplate() {
        return new VerifyTemplate();
    }
}
