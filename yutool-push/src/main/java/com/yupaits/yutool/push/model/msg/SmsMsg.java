package com.yupaits.yutool.push.model.msg;

import com.google.common.collect.Maps;
import com.yupaits.yutool.push.support.IMsgTemplate;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 短信消息
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class SmsMsg implements IMsg {
    private static final long serialVersionUID = 1L;

    /**
     * 短信模板
     */
    private IMsgTemplate msgTemplate;

    /**
     * 短信模板参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    @Override
    public boolean isValid() {
        return msgTemplate != null;
    }

    @Override
    public SmsMsg putParam(String key, Object value) {
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public SmsMsg removeParam(String key) {
        if (this.params != null) {
            this.params.remove(key);
        }
        return this;
    }
}
