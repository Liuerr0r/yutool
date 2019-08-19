package com.yupaits.yutool.mq.core;

/**
 * 消息队列配置接口
 * @author yupaits
 * @date 2019/7/18
 */
public interface IQueueEnum {

    /**
     * 获取Exchange名称
     * @return 交换机名称
     */
    String getExchange();

    /**
     * 获取队列名称
     * @return 队列名称
     */
    String getName();

    /**
     * 获取路由键名称
     * @return 路由键名称
     */
    String getRouteKey();
}
