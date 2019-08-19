package com.yupaits.yutool.commons.service;

/**
 * 操作人接口
 * @author yupaits
 * @date 2019/7/15
 */
public interface OptService {

    /**
     * 获取操作人ID
     * @return 操作人ID
     */
    String getOperatorId();

    /**
     * 根据ID获取操作人用户名
     * @param operatorId 操作人ID
     * @return 操作人用户名
     */
    String getOptName(String operatorId);
}
