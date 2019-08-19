package com.yupaits.yutool.push.support.notification;


import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.msg.Notification;
import com.yupaits.yutool.push.support.PushProps;
import com.yupaits.yutool.template.exception.TemplateException;

import java.io.IOException;

/**
 * 发送APP通知服务商接口
 * @author yupaits
 * @date 2019/7/22
 */
public interface NotificationProvider {

    /**
     * 推送APP通知
     * @param notification APP通知
     * @param pushProps PushProps参数
     * @throws PushException 抛出PushException
     * @throws IOException 抛出IOException
     * @throws TemplateException 抛出TemplateException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    void pushNotification(Notification notification, PushProps pushProps) throws PushException, TemplateException, IOException, freemarker.template.TemplateException;
}
