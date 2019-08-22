package com.yupaits.yutool.plugin.auditlogger.vo;

import com.yupaits.yutool.orm.base.BaseVo;
import com.yupaits.yutool.plugin.auditlogger.support.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审计记录Vo对象
 * @author yupaits
 * @date 2019/8/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "AuditRecordVo对象")
public class AuditRecordVo extends BaseVo<Long> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据表名")
    private String tableName;

    @ApiModelProperty(value = "审计数据ID")
    private String recordId;

    @ApiModelProperty(value = "字段名")
    private String fieldName;

    @ApiModelProperty(value = "字段描述")
    private String fieldDescription;

    @ApiModelProperty(value = "更新之前的值")
    private String oldValue;

    @ApiModelProperty(value = "更新之后的值")
    private String newValue;

    @ApiModelProperty(value = "审批状态")
    private AuditStatus status;
}
