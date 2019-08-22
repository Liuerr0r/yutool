package com.yupaits.yutool.plugin.auditlogger.config;

import com.yupaits.yutool.orm.support.AuditLogger;
import com.yupaits.yutool.plugin.auditlogger.core.DefaultAuditLogger;
import com.yupaits.yutool.plugin.auditlogger.mapper.AuditRecordMapper;
import com.yupaits.yutool.plugin.auditlogger.service.AuditRecordService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 审计日志自动装配
 * @author yupaits
 * @date 2019/7/25
 */
@Configuration
@ComponentScan(basePackageClasses = AuditRecordService.class)
@MapperScan(basePackageClasses = AuditRecordMapper.class)
public class AuditLoggerAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public AuditLogger auditLogger(AuditRecordService auditRecordService) {
        return new DefaultAuditLogger(auditRecordService);
    }
}
