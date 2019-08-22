package com.yupaits.yutool.plugin.auditlogger.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yupaits.yutool.orm.base.BaseModel;
import com.yupaits.yutool.plugin.auditlogger.support.AuditStatus;
import lombok.*;

/**
 * 审计记录实体
 * @author yupaits
 * @date 2019/7/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("audit_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditRecord extends BaseModel<Long, AuditRecord> {
    private static final long serialVersionUID = 1L;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 审计数据ID
     */
    private String recordId;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段描述
     */
    private String fieldDescription;

    /**
     * 更新之前的值
     */
    private String oldValue;

    /**
     * 更新之后的值
     */
    private String newValue;

    /**
     * 审批状态
     */
    private AuditStatus status;
}
