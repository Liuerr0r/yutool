package com.yupaits.yutool.verify.support;

import com.yupaits.yutool.push.support.IMsgTemplate;

/**
 * 短信场景接口
 * @author yupaits
 * @date 2019/8/27
 */
public interface ISmsScene {

    /**
     * 获取短信场景码
     * @return 短信场景码
     */
    int getSceneCode();

    /**
     * 获取短信场景标识
     * @return 短信场景标识
     */
    String getScene();

    /**
     * 获取短信场景描述
     * @return 短信场景描述
     */
    String getSceneDesc();

    /**
     * 获取短信消息模板
     * @return 短信消息模板
     */
    IMsgTemplate getSmsTemplate();
}
