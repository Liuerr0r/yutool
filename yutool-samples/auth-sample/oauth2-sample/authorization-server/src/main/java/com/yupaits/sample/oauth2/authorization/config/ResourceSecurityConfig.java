package com.yupaits.sample.oauth2.authorization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

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

                })
                .accessDeniedHandler((request, response, accessDeniedEx) -> {

                })
                .and()
                .authorizeRequests()
                .antMatchers("").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }
}
