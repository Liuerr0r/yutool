package com.yupaits.yutool.orm.support;

import com.posun.cmpt.orm.base.AbstractModel;

import java.io.Serializable;

/**
 * 审计日志接口
 * @author yupaits
 * @date 2019/7/16
 */
public interface AuditLogger {

    /**
     * 记录审计日志
     * @param auditEvent 审计事件
     */
    <ID extends Serializable, T extends AbstractModel<ID, T>> void auditLog(AuditEvent<T> auditEvent);
}
