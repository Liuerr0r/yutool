package com.yupaits.yutool.orm.support;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页查询对象
 * @author yupaits
 * @date 2019/8/1
 */
@Data
@ApiModel(description = "分页查询")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排序参数")
    private List<OrderItem> orders = Lists.newArrayList();

    @ApiModelProperty("自定义查询参数")
    private Map<String, Object> query = Maps.newHashMap();

    @ApiModelProperty("聚合信息查询参数")
    private List<AggregateField> aggregates = Lists.newArrayList();

    /**
     * 查询对象整理
     */
    public void collate() {
        orders.removeIf(orderItem -> StringUtils.isBlank(orderItem.getColumn()));
        query.entrySet().removeIf(entry -> StringUtils.isBlank(entry.getKey()));
        aggregates.removeIf(aggregateField -> StringUtils.isBlank(aggregateField.getColumn()) || aggregateField.getType() == null);
    }
}
