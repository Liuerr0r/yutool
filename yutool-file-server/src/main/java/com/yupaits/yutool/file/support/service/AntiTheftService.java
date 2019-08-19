package com.yupaits.yutool.file.support.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 防盗链服务接口
 * @author yupaits
 * @date 2019/7/23
 */
public interface AntiTheftService {
    /**
     * 检查是否盗链
     * @param request 请求体
     * @param response 响应体
     * @return 检查是否通过 true-非盗链
     */
    boolean check(HttpServletRequest request, HttpServletResponse response);
}
