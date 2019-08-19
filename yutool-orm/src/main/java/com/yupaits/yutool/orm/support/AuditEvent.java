package com.yupaits.yutool.orm.support;

import com.posun.cmpt.orm.base.AbstractModel;
import lombok.Builder;
import lombok.Data;

/**
 * 审计事件
 * @author yupaits
 * @date 2019/7/16
 */
@Data
@Builder
public class AuditEvent<T extends AbstractModel> {

    /**
     * 源记录
     */
    private T source;

    /**
     * 目标记录
     */
    private T target;

}
