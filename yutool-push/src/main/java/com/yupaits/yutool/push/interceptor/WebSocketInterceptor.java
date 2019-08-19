package com.yupaits.yutool.push.interceptor;

import com.yupaits.yutool.push.support.webmsg.WebMsgHandler;
import com.yupaits.yutool.push.support.webmsg.WebMsgUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * WebSocket拦截器
 * @author yupaits
 * @date 2019/7/26
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    private WebMsgUserService webMsgUserService;

    public WebSocketInterceptor(WebMsgUserService webMsgUserService) {
        this.webMsgUserService = webMsgUserService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(true);
            if (session != null) {
                String webMsgUser = webMsgUserService.getWebMsgUserName(request);
                if (StringUtils.isNotBlank(webMsgUser)) {
                    attributes.put(WebMsgHandler.USER_KEY, webMsgUser);
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
    }
}
