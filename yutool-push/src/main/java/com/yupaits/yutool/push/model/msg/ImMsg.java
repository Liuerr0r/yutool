package com.yupaits.yutool.push.model.msg;

import com.google.common.collect.Maps;
import com.yupaits.yutool.push.support.IMsgTemplate;
import com.yupaits.yutool.push.support.im.ImMsgType;
import com.yupaits.yutool.push.support.im.ImSendType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * IM消息
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class ImMsg implements IMsg {
    private static final long serialVersionUID = 1L;

    /**
     * 消息发送者
     */
    private String from;

    /**
     * IM消息类型
     */
    private ImMsgType type = ImMsgType.TEXT;

    /**
     * IM消息发送类型
     */
    private ImSendType sendType;

    /**
     * IM消息模板
     */
    private IMsgTemplate msgTemplate;

    /**
     * IM消息模板参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 拓展参数
     */
    private Map<String, Object> extras = Maps.newHashMap();

    @Override
    public boolean isValid() {
        return msgTemplate != null && StringUtils.isNotBlank(from) && sendType != null;
    }

    @Override
    public ImMsg putParam(String key, Object value) {
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public ImMsg removeParam(String key) {
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
    public ImMsg putExtra(String key, Object value) {
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
    public ImMsg removeExtra(String key) {
        if (this.extras != null) {
            this.extras.remove(key);
        }
        return this;
    }
}
