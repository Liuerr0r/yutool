package com.yupaits.yutool.push.model.msg;

import com.google.common.collect.Maps;
import com.yupaits.yutool.push.support.IMsgTemplate;
import com.yupaits.yutool.push.support.notification.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * APP通知
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class Notification implements IMsg {
    private static final long serialVersionUID = 1L;

    /**
     * APP通知模板
     */
    private IMsgTemplate msgTemplate;

    /**
     * APP通知模板参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 通知标题
     */
    private String title;

    /**
     * 是否是APP通知
     */
    private boolean appNotify;

    /**
     * 通知类型
     */
    private NotificationType type = NotificationType.NOTIFY;

    /**
     * 点击跳转url
     */
    private String url;

    @Override
    public boolean isValid() {
        return msgTemplate != null && StringUtils.isNotBlank(title) && type != null && (type != NotificationType.LINK || StringUtils.isNotBlank(url));
    }

    @Override
    public Notification putParam(String key, Object value) {
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public Notification removeParam(String key) {
        if (this.params != null) {
            this.params.remove(key);
        }
        return this;
    }
}
