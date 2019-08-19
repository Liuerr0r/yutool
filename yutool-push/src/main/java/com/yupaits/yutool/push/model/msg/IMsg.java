package com.yupaits.yutool.push.model.msg;


import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.support.IMsgTemplate;

import java.io.Serializable;
import java.util.Map;

/**
 * 统一消息体接口
 * @author yupaits
 * @date 2019/7/19
 */
public interface IMsg extends Serializable {

    /**
     * 获取消息模板
     * @return 消息模板
     */
    IMsgTemplate getMsgTemplate();

    /**
     * 获取模板参数
     * @return 模板参数
     */
    Map<String, Object> getParams();

    /**
     * 消息校验
     * @return 校验结果
     */
    boolean isValid();

    /**
     * 设置模板参数
     * @param key 模板参数名
     * @param value 模板参数值
     * @return 当前消息对象
     */
    IMsg putParam(String key, Object value);

    /**
     * 移除模板参数
     * @param key 模板参数名
     * @return 当前消息对象
     */
    IMsg removeParam(String key);

    /**
     * 转成其它类型的消息，以支持多渠道推送功能
     * 实际的业务开发时，一般是新写一个消息类型并重写该方法以满足转成所需目标类型的需求
     * @param targetMsgClass 目标消息类型Class对象
     * @param <T> 目标消息类型
     * @return 目标消息对象
     * @throws PushException 抛出PushException
     */
    default <T extends IMsg> T toMsg(Class<T> targetMsgClass) throws PushException {
        return null;
    }
}
