package com.yupaits.yutool.plugin.auditlogger.service.impl;

import com.yupaits.yutool.commons.exception.BusinessException;
import com.yupaits.yutool.commons.service.OptService;
import com.yupaits.yutool.orm.base.BaseResultServiceImpl;
import com.yupaits.yutool.plugin.auditlogger.mapper.AuditRecordMapper;
import com.yupaits.yutool.plugin.auditlogger.model.AuditRecord;
import com.yupaits.yutool.plugin.auditlogger.service.AuditRecordService;
import com.yupaits.yutool.plugin.auditlogger.support.AuditLoggerResultCode;
import com.yupaits.yutool.plugin.auditlogger.support.AuditStatus;
import com.yupaits.yutool.plugin.auditlogger.vo.AuditRecordVo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

/**
 * 审计记录Service实现
 * @author yupaits
 * @date 2019/7/25
 */
@Service
public class AuditRecordServiceImpl extends BaseResultServiceImpl<Long, AuditRecord, AuditRecordMapper> implements AuditRecordService {
    private static final String UNDERLINE = "_";
    private static final String UPDATE_SQL = "update %s set %s='%s' where id=%s";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    protected AuditRecordServiceImpl(OptService optService, JdbcTemplate jdbcTemplate) {
        super(AuditRecord.class, optService, null);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setDefaultVoConfig() {
        setVoClass(AuditRecordVo.class);
    }

    @Override
    public void setDefaultModelBuilder() {
    }

    @Override
    public boolean approve(Long auditRecordId) throws BusinessException {
        AuditRecord auditRecord = getById(auditRecordId);
        if (auditRecord.getStatus() != AuditStatus.AUDITING) {
            throw BusinessException.from(AuditLoggerResultCode.STATUS_ERROR);
        }
        //更新字段
        updateAuditField(auditRecord);
        auditRecord.setStatus(AuditStatus.APPROVED);
        return updateById(auditRecord);
    }

    /**
     * 更新审计字段的值
     * @param auditRecord 审计记录
     * @throws BusinessException 抛出BusinessException
     */
    private void updateAuditField(AuditRecord auditRecord) throws BusinessException {
        String sql = null;
        try {
            sql = String.format(UPDATE_SQL, auditRecord.getTableName(), auditRecord.getFieldName(),
                    auditRecord.getNewValue(), auditRecord.getRecordId());
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            throw BusinessException.from(AuditLoggerResultCode.UPDATE_FAIL, true, e, String.format("更新语句：%s", sql));
        }
    }

    @Override
    public boolean reject(Long auditRecordId) throws BusinessException {
        AuditRecord auditRecord = getById(auditRecordId);
        if (auditRecord.getStatus() != AuditStatus.AUDITING) {
            throw BusinessException.from(AuditLoggerResultCode.STATUS_ERROR);
        }
        auditRecord.setStatus(AuditStatus.REJECTED);
        return updateById(auditRecord);
    }

    /**
     * 驼峰式转下划线
     * @param camelStr 驼峰式命名
     * @return 下划线命名
     */
    private String camelToUnderlineCase(String camelStr) {
        String[] strArray = StringUtils.splitByCharacterTypeCamelCase(camelStr);
        StringJoiner joiner = new StringJoiner(UNDERLINE);
        if (ArrayUtils.isNotEmpty(strArray)) {
            for (String str : strArray) {
                String underlineStr = StringUtils.lowerCase(str);
                if (underlineStr != null) {
                    joiner.add(underlineStr);
                }
            }
        }
        return joiner.toString();
    }
}
