package com.yupaits.yutool.verify.config;

import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.push.core.PushTemplate;
import com.yupaits.yutool.verify.core.VerifyTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码服务自动装配
 * @author yupaits
 * @date 2019/8/19
 */
@Configuration
@EnableConfigurationProperties(CodeSmsProps.class)
public class VerifyAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({PushTemplate.class, CacheTemplate.class, CodeSmsProps.class})
    public VerifyTemplate verifyTemplate(PushTemplate pushTemplate, CacheTemplate cacheTemplate, CodeSmsProps codeSmsProps) {
        return new VerifyTemplate(pushTemplate, cacheTemplate, codeSmsProps);
    }
}
