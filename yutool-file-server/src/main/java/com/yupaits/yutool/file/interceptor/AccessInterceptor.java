package com.yupaits.yutool.file.interceptor;

import com.yupaits.yutool.file.support.service.FileAccessService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验拦截器
 * @author yupaits
 * @date 2019/7/23
 */
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private final FileAccessService fileAccessService;

    public AccessInterceptor(FileAccessService fileAccessService) {
        this.fileAccessService = fileAccessService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return fileAccessService.checkAccess(request, response);
    }
}
