package com.yupaits.yutool.plugin.jwt.config;

import com.yupaits.yutool.plugin.jwt.support.JwtHelper;
import com.yupaits.yutool.plugin.jwt.support.JwtProps;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JwtHelper自动装配
 * @author yupaits
 * @date 2019/8/22
 */
@Configuration
@EnableConfigurationProperties(JwtProps.class)
public class JwtAutoConfigure {

    @Bean
    public JwtHelper jwtHelper(JwtProps jwtProps) {
        return new JwtHelper(jwtProps);
    }
}
