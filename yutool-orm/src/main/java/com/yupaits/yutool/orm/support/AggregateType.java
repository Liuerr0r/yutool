package com.yupaits.yutool.orm.support;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * 聚合类型
 * @author yupaits
 * @date 2019/7/24
 */
public enum AggregateType {
    /**
     * 求和
     */
    SUM,
    /**
     * 计数
     */
    COUNT,
    /**
     * 求平均值
     */
    AVERAGE,
    /**
     * 最大值
     */
    MAX,
    /**
     * 最小值
     */
    MIN;

    /**
     * 根据名称获取聚合类型
     * @return 聚合类型
     */
    @JsonCreator
    public static AggregateType fromName(String name) {
        AggregateType[] values = AggregateType.values();
        for (AggregateType aggregateType : values) {
            if (StringUtils.equalsIgnoreCase(aggregateType.name(), name)) {
                return aggregateType;
            }
        }
        return null;
    }
}
