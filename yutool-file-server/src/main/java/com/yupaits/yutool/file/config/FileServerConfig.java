package com.yupaits.yutool.file.config;

import com.yupaits.yutool.file.interceptor.AccessInterceptor;
import com.yupaits.yutool.file.interceptor.AntiTheftInterceptor;
import com.yupaits.yutool.file.support.service.AntiTheftService;
import com.yupaits.yutool.file.support.service.FileAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 防盗链配置类
 * @author yupaits
 * @date 2019/7/23
 */
@Configuration
@ConditionalOnBean({AntiTheftService.class, FileAccessService.class})
public class FileServerConfig implements WebMvcConfigurer {
    private static final String FILE_SERVER_PATH = "/upload/**";

    private final AntiTheftService antiTheftService;
    private final FileAccessService fileAccessService;

    @Autowired
    public FileServerConfig(AntiTheftService antiTheftService, FileAccessService fileAccessService) {
        this.antiTheftService = antiTheftService;
        this.fileAccessService = fileAccessService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AntiTheftInterceptor(antiTheftService)).addPathPatterns(FILE_SERVER_PATH);
        registry.addInterceptor(new AccessInterceptor(fileAccessService)).addPathPatterns(FILE_SERVER_PATH);
    }
}
