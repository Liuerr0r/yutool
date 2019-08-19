package com.yupaits.yutool.commons.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * RESTful API响应结果
 * @author yupaits
 * @date 2019/7/15
 */
@Data
@Builder
@ApiModel
public class Result<T> implements Serializable {

    @ApiModelProperty("返回码")
    private int code;

    @ApiModelProperty("返回信息")
    private String message;

    @ApiModelProperty("返回数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @ApiModelProperty("是否成功")
    public boolean isSuccess() {
        return code == HttpStatus.OK.value();
    }
}
