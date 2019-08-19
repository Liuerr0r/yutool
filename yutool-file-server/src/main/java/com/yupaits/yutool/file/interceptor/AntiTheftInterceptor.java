package com.yupaits.yutool.file.interceptor;

import com.yupaits.yutool.file.support.service.AntiTheftService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件防盗链拦截器
 * @author yupaits
 * @date 2019/7/23
 */
public class AntiTheftInterceptor extends HandlerInterceptorAdapter {

    private final AntiTheftService antiTheftService;

    public AntiTheftInterceptor(AntiTheftService antiTheftService) {
        this.antiTheftService = antiTheftService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return antiTheftService.check(request, response);
    }
}
