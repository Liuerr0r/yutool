package com.yupaits.yutool.push.model;

import com.yupaits.yutool.push.model.msg.IMsg;
import com.yupaits.yutool.push.support.PushProps;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 推送消息载体
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@AllArgsConstructor
public class MsgPayload implements Serializable {
    private static final long serialVersionUID = 992406929339665222L;

    /**
     * 消息体
     */
    private IMsg msg;

    /**
     * 推送配置
     */
    private PushProps pushProps;

    /**
     * 校验参数
     * @return 校验通过
     */
    public boolean isValid() {
        return msg != null && msg.isValid() && pushProps != null && pushProps.isValid();
    }
}
