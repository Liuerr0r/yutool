package com.yupaits.yutool.plugin.auditlogger.support;

import com.yupaits.yutool.commons.result.IResultCode;
import lombok.Getter;

/**
 * @author yupaits
 * @date 2019/8/8
 */
@Getter
public enum AuditLoggerResultCode implements IResultCode {
    /**
     * 状态错误
     */
    STATUS_ERROR(40001, "只能审核状态为待审批的记录"),
    UPDATE_FAIL(40002, "更新审计字段的值失败");

    private int code;
    private String message;

    AuditLoggerResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
