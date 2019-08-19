package com.yupaits.yutool.orm.support;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author yupaits
 * @date 2019/7/15
 */
public interface IAggregatePage<T> extends IPage<T> {

    /**
     * 获取聚合信息结果
     * @return 聚合信息结果列表
     */
    List<AggregateResult> getAggregates();
}
