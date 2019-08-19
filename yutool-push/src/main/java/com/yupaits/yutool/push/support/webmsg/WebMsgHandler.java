package com.yupaits.yutool.push.support.webmsg;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author yupaits
 * @date 2019/7/26
 */
@Slf4j
@Service
public class WebMsgHandler implements WebSocketHandler {
    public static final String USER_KEY = "userId";
    private static final Map<String, WebSocketSession> USERS = Maps.newConcurrentMap();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket连接成功");
        if (session.getAttributes().containsKey(USER_KEY)) {
            USERS.put(session.getAttributes().get(USER_KEY).toString(), session);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (session.isOpen()) {
            session.sendMessage(message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.error("WebSocket连接出现错误：{}", throwable.toString());
        if (session.getAttributes().containsKey(USER_KEY)) {
            USERS.remove(session.getAttributes().get(USER_KEY).toString());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("WebSocket连接已关闭");
        if (session.getAttributes().containsKey(USER_KEY)) {
            USERS.remove(session.getAttributes().get(USER_KEY).toString());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageToUser(String userId, TextMessage message) {
        WebSocketSession session = USERS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
                log.info("发送WebSocket消息成功，接收消息用户：{}", userId);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
