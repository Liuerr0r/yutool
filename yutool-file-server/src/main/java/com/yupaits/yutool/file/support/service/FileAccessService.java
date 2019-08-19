package com.yupaits.yutool.file.support.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件访问权限服务接口
 * @author yupaits
 * @date 2019/7/23
 */
public interface FileAccessService {

    /**
     * 检查文件访问权限
     * @param request 请求体
     * @param response 响应体
     * @return 检查结果 true-拥有访问权限
     */
    boolean checkAccess(HttpServletRequest request, HttpServletResponse response);
}
