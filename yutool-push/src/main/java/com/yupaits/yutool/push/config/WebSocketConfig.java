package com.yupaits.yutool.push.config;

import com.yupaits.yutool.push.interceptor.WebSocketInterceptor;
import com.yupaits.yutool.push.support.webmsg.WebMsgHandler;
import com.yupaits.yutool.push.support.webmsg.WebMsgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置
 * @author yupaits
 * @date 2019/7/26
 */
@Configuration
@ConditionalOnBean(WebMsgUserService.class)
@EnableWebSocket
@ComponentScan(basePackageClasses = WebMsgHandler.class)
public class WebSocketConfig implements WebSocketConfigurer, WebMvcConfigurer {

    private final WebMsgHandler webMsgHandler;
    private final WebMsgUserService webMsgUserService;

    @Autowired
    public WebSocketConfig(WebMsgHandler webMsgHandler, WebMsgUserService webMsgUserService) {
        this.webMsgHandler = webMsgHandler;
        this.webMsgUserService = webMsgUserService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webMsgHandler, "/webmsg").addInterceptors(new WebSocketInterceptor(webMsgUserService)).setAllowedOrigins("*");
        registry.addHandler(webMsgHandler, "/sockjs/webmsg").addInterceptors(new WebSocketInterceptor(webMsgUserService)).setAllowedOrigins("*").withSockJS();
    }
}
