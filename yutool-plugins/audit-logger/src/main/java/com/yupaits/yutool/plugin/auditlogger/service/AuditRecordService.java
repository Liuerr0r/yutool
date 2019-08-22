package com.yupaits.yutool.plugin.auditlogger.service;

import com.yupaits.yutool.commons.exception.BusinessException;
import com.yupaits.yutool.orm.base.IBaseService;
import com.yupaits.yutool.plugin.auditlogger.model.AuditRecord;

/**
 * 审计记录Service
 * @author yupaits
 * @date 2019/7/25
 */
public interface AuditRecordService extends IBaseService {

    /**
     * 保存审计记录
     * @param auditRecord 审计记录
     * @return 保存成功
     */
    boolean save(AuditRecord auditRecord);

    /**
     * 审计记录审批通过
     * @param auditRecordId 审计记录ID
     * @return 审批通过
     * @throws BusinessException 抛出BusinessException
     */
    boolean approve(Long auditRecordId) throws BusinessException;

    /**
     * 审计记录审批驳回
     * @param auditRecordId 审计记录ID
     * @return 审批驳回
     * @throws BusinessException 抛出BusinessException
     */
    boolean reject(Long auditRecordId) throws BusinessException;
}
