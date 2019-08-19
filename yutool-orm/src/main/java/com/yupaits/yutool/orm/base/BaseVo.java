package com.yupaits.yutool.orm.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.posun.cmpt.commons.constant.DateTimeConstants;
import com.posun.cmpt.commons.utils.serializer.IdSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Vo基类
 * @author yupaits
 * @date 2019/7/15
 */
@Data
public class BaseVo<ID extends Serializable> implements Serializable {

    @ApiModelProperty("ID")
    @JsonSerialize(using = IdSerializer.class)
    private ID id;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = DateTimeConstants.DEFAULT_DATETIME_PATTERN)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime createdTime;

    @ApiModelProperty("创建人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdBy;

    @ApiModelProperty("创建人用户名")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdByName;

    @ApiModelProperty("最近更新时间")
    @JsonFormat(pattern = DateTimeConstants.DEFAULT_DATETIME_PATTERN)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime lastModifiedTime;

    @ApiModelProperty("最近更新人ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastModifiedBy;

    @ApiModelProperty("最近更新人用户名")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastModifiedByName;
}
