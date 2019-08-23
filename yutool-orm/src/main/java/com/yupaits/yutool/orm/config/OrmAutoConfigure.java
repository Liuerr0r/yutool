package com.yupaits.yutool.orm.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.yupaits.yutool.orm.support.service.MetaObjectOptService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ORM自动装配
 * @author yupaits
 * @date 2019/7/25
 */
@Configuration
public class OrmAutoConfigure {

    /**
     * 公共字段自动写入
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MetaObjectHandler.class)
    @ConditionalOnBean(MetaObjectOptService.class)
    public MetaObjectHandler metaObjectHandler(MetaObjectOptService metaObjectOptService) {
        return new MetaObjectHandlerImpl(metaObjectOptService);
    }

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁插件
     */
    @Bean
    @ConditionalOnMissingBean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
