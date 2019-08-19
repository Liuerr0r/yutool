package com.yupaits.yutool.push.support.webmsg;

import org.springframework.http.server.ServerHttpRequest;

/**
 * WebSocket用户服务接口
 * @author yupaits
 * @date 2019/7/26
 */
public interface WebMsgUserService {

    /**
     * 获取WebSocket用户名
     * @param request 请求体
     * @return WebSocket用户名
     */
    String getWebMsgUserName(ServerHttpRequest request);
}
