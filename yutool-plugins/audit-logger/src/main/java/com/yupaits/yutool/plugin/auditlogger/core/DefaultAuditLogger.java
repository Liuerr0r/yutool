package com.yupaits.yutool.plugin.auditlogger.core;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Maps;
import com.yupaits.yutool.orm.annotation.AuditLog;
import com.yupaits.yutool.orm.base.AbstractModel;
import com.yupaits.yutool.orm.support.AuditEvent;
import com.yupaits.yutool.orm.support.AuditLogger;
import com.yupaits.yutool.plugin.auditlogger.model.AuditRecord;
import com.yupaits.yutool.plugin.auditlogger.service.AuditRecordService;
import com.yupaits.yutool.plugin.auditlogger.support.AuditMetaInfo;
import com.yupaits.yutool.plugin.auditlogger.support.AuditStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 默认的审计日志实现
 * @author yupaits
 * @date 2019/7/25
 */
@Slf4j
public class DefaultAuditLogger implements AuditLogger {
    private static final Map<Class, AuditMetaInfo> AUDIT_META_INFO_MAP = Maps.newConcurrentMap();

    private final AuditRecordService auditRecordService;

    public DefaultAuditLogger(AuditRecordService auditRecordService) {
        this.auditRecordService = auditRecordService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <ID extends Serializable, T extends AbstractModel<ID, T>> void auditLog(AuditEvent<T> auditEvent) {
        T source = auditEvent.getSource();
        T target = auditEvent.getTarget();
        Class<T> tClass = (Class<T>) target.getClass();
        AuditMetaInfo auditMetaInfo;
        if (AUDIT_META_INFO_MAP.containsKey(tClass)) {
            auditMetaInfo = AUDIT_META_INFO_MAP.get(tClass);
        } else {
            auditMetaInfo = new AuditMetaInfo();
            if (tClass.isAnnotationPresent(TableName.class)) {
                TableName tableNameAnnotation = tClass.getAnnotation(TableName.class);
                auditMetaInfo.setTableName(tableNameAnnotation.value());
            }
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AuditLog.class)) {
                    field.setAccessible(true);
                    auditMetaInfo.getAuditFields().add(field);
                    auditMetaInfo.getFieldDescriptionMap().put(field.getName(), field.getAnnotation(AuditLog.class).description());
                }
            }
            AUDIT_META_INFO_MAP.put(tClass, auditMetaInfo);
        }
        //待更新记录ID
        ID id = target.getId();
        for (Field field : auditMetaInfo.getAuditFields()) {
            Object sourceValue;
            Object targetValue;
            try {
                sourceValue = field.get(source);
                targetValue = field.get(target);
            } catch (IllegalAccessException e) {
                log.error("获取{}属性{}的值失败", tClass.getCanonicalName(), field.getName(), e);
                continue;
            }
            if (ObjectUtils.anyNotNull(sourceValue, targetValue) && ObjectUtils.notEqual(sourceValue, targetValue)) {
                String fieldName = field.getName();
                AuditRecord auditRecord = AuditRecord.builder()
                        .tableName(auditMetaInfo.getTableName())
                        .recordId(String.valueOf(id))
                        .fieldName(fieldName)
                        .fieldDescription(auditMetaInfo.getFieldDescriptionMap().get(fieldName))
                        .oldValue(targetValue instanceof String ? (String) targetValue : String.valueOf(targetValue))
                        .newValue(sourceValue instanceof String ? (String) sourceValue : String.valueOf(sourceValue))
                        .status(AuditStatus.AUDITING)
                        .build();
                if (!auditRecordService.save(auditRecord)) {
                    log.error("保存审计记录失败，审计信息: {}", auditRecord);
                }
            }
        }
        BeanUtils.copyProperties(source, target, (String[]) auditMetaInfo.getAuditFields().stream().map(Field::getName).toArray(String[]::new));
    }
}
