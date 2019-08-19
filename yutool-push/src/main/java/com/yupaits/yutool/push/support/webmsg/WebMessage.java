package com.yupaits.yutool.push.support.webmsg;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 推送的Web网页消息内容
 * @author yupaits
 * @date 2019/7/26
 */
@Data
@Builder
public class WebMessage implements Serializable {
    private static final long serialVersionUID = -4383626566327215302L;

    /**
     * 推送触发操作
     */
    private IAction action;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 拓展参数
     */
    private Map<String, Object> extras;
}
