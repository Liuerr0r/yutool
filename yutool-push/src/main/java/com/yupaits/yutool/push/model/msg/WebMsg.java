package com.yupaits.yutool.push.model.msg;

import com.google.common.collect.Maps;
import com.yupaits.yutool.push.support.IMsgTemplate;
import com.yupaits.yutool.push.support.webmsg.IAction;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Web网页消息
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class WebMsg implements IMsg {
    private static final long serialVersionUID = 1L;

    /**
     * 推送触发操作
     */
    private IAction action;

    /**
     * 消息标题
     */
    private String title;

    /**
     * Web网页消息模板
     */
    private IMsgTemplate msgTemplate;

    /**
     * Web网页消息模板参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 拓展参数
     */
    private Map<String, Object> extras = Maps.newHashMap();

    @Override
    public boolean isValid() {
        return msgTemplate != null && action != null && StringUtils.isNotBlank(title);
    }

    @Override
    public WebMsg putParam(String key, Object value) {
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public WebMsg removeParam(String key) {
        if (this.params != null) {
            this.params.remove(key);
        }
        return this;
    }

    /**
     * 设置拓展参数
     * @param key 参数名
     * @param value 参数值
     * @return 当前对象
     */
    public WebMsg putExtra(String key, Object value) {
        if (this.extras == null) {
            this.extras = Maps.newHashMap();
        }
        this.extras.put(key, value);
        return this;
    }

    /**
     * 移除拓展参数
     * @param key 参数名
     * @return 当前对象
     */
    public WebMsg removeExtra(String key) {
        if (this.params != null) {
            this.params.remove(key);
        }
        return this;
    }
}
