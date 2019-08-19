package com.yupaits.yutool.orm.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 携带聚合信息的分页数据
 * @author yupaits
 * @date 2019/7/15
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AggregatePage<T> extends Page<T> implements IAggregatePage<T> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public AggregatePage(IPage page) {
        this.setTotal(page.getTotal());
        this.setPages(page.getPages());
        this.setCurrent(page.getCurrent());
        this.setSize(page.getSize());
        this.setOrders(page.orders());
        this.setRecords(page.getRecords());
        this.setSearchCount(page.isSearchCount());
        this.setOptimizeCountSql(page.optimizeCountSql());
    }

    /**
     * 聚合信息结果列表
     */
    private List<AggregateResult> aggregates;
}
