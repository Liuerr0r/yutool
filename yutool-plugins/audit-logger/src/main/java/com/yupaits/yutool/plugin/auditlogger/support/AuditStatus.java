package com.yupaits.yutool.plugin.auditlogger.support;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 审批状态
 * @author yupaits
 * @date 2019/7/25
 */
public enum AuditStatus implements IEnum<String> {
    /**
     * 待审批
     */
    AUDITING,
    /**
     * 审批通过
     */
    APPROVED,
    /**
     * 审批驳回
     */
    REJECTED;

    @Override
    public String getValue() {
        return this.name();
    }
}
