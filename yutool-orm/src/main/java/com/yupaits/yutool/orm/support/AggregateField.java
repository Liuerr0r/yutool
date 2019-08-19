package com.yupaits.yutool.orm.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 聚合信息字段
 * @author yupaits
 * @date 2019/7/24
 */
@Data
@AllArgsConstructor
@ApiModel(description = "聚合信息字段")
public class AggregateField implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段名")
    private String column;

    @ApiModelProperty(value = "聚合类型")
    private AggregateType type;
}
