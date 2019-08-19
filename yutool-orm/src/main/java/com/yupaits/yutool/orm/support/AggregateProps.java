package com.yupaits.yutool.orm.support;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据聚合信息配置类
 * @author yupaits
 * @date 2019/7/15
 */
@Data
public class AggregateProps implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 聚合信息字段列表
     */
    private List<AggregateField> aggregates = Lists.newArrayList();
}
