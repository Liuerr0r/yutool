package com.yupaits.yutool.push.support;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 推送配置
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Builder
public class PushProps implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否延迟推送
     */
    private boolean delayed;

    /**
     * 延迟推送时间毫秒数
     */
    private long delayMillis;

    /**
     * 目标租户ID
     */
    private String tenantId;

    /**
     * 消息接收人
     */
    private MultiValueMap<PushType, String> receivers;

    /**
     * 抄送给
     */
    private Set<String> cc;

    /**
     * 暗抄送
     */
    private Set<String> bcc;

    /**
     * 校验参数
     * @return 校验结果
     */
    public boolean isValid() {
        if (delayed && delayMillis < 0L) {
            return false;
        }
        if (MapUtils.isEmpty(receivers)) {
            return false;
        }
        for (Map.Entry<PushType, List<String>> entry : receivers.entrySet()) {
            if (entry.getKey() == null || CollectionUtils.isEmpty(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
