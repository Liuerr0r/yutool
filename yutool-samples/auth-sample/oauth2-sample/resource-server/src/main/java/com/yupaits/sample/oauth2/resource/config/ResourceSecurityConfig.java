package com.yupaits.sample.oauth2.resource.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.sample.oauth2.resource.constant.SecurityConstants;
import com.yupaits.yutool.commons.result.IResultCode;
import com.yupaits.yutool.commons.result.ResultCode;
import com.yupaits.yutool.commons.result.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yupaits
 * @date 2019/8/24
 */
@Configuration
@EnableResourceServer
public class ResourceSecurityConfig extends ResourceServerConfigurerAdapter {
    private final ObjectMapper objectMapper;

    @Autowired
    public ResourceSecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authEx) -> {
                    failCodeResult(response, ResultCode.UNAUTHORIZED);
                })
                .accessDeniedHandler((request, response, accessDeniedEx) -> {
                    failCodeResult(response, ResultCode.FORBIDDEN);
                })
                .and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.ignorePaths).permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }

    /**
     * 将响应内容写入response
     * @param response 响应体
     * @param resultCode 响应码内容
     * @throws IOException 抛出IOException
     */
    private void failCodeResult(HttpServletResponse response, IResultCode resultCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(response.getWriter(), ResultWrapper.fail(resultCode));
    }
}
