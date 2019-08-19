package com.yupaits.yutool.orm.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 聚合信息结果
 * @author yupaits
 * @date 2019/7/24
 */
@Data
@AllArgsConstructor
public class AggregateResult<T> {

    /**
     * 字段名
     */
    private String field;

    /**
     * 聚合类型
     */
    private AggregateType type;

    /**
     * 聚合结果
     */
    private T value;
}
