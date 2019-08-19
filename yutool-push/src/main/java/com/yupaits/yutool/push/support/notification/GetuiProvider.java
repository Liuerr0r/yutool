package com.yupaits.yutool.push.support.notification;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Message;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.google.common.collect.Lists;
import com.yupaits.yutool.push.exception.PushException;
import com.yupaits.yutool.push.model.msg.Notification;
import com.yupaits.yutool.push.support.PushProps;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.template.exception.TemplateException;
import com.yupaits.yutool.template.utils.TemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 个推APP通知推送
 * Java服务端开发文档：http://docs.getui.com/getui/server/java/start/
 * @author yupaits
 * @date 2019/7/26
 */
@Slf4j
public class GetuiProvider implements NotificationProvider {
    private static final String PUSH_RESULT_KEY = "result";
    private static final String PUSH_SUCCESS = "ok";

    @Value("${getui.appId:}")
    private String appId;
    @Value("${getui.appKey:}")
    private String appKey;
    @Value("${getui.masterSecret:}")
    private String masterSecret;
    @Value("${getui.useSsl:false}")
    private boolean useSsl;
    @Value("${getui.offlineExpireTime:86400000}")
    private long offlineExpireTime;
    @Value("${getui.appPushSpeed:100}")
    private int appPushSpeed;

    @Override
    public void pushNotification(Notification notification, PushProps pushProps) throws PushException, TemplateException, IOException, freemarker.template.TemplateException {
        IGtPush push = new IGtPush(appKey, masterSecret, useSsl);
        IPushResult result;
        if (notification.isAppNotify()) {
            //向指定应用推送消息
            AppMessage message = new AppMessage();
            message.setAppIdList(Lists.newArrayList(appId));
            message.setSpeed(appPushSpeed);
            buildMessage(notification, message);
            result = push.pushMessageToApp(message);
        } else {
            //向指定列表用户推送消息
            ListMessage message = new ListMessage();
            buildMessage(notification, message);
            List<Target> targets = pushProps.getReceivers().getOrDefault(PushType.NOTIFICATION, Lists.newArrayList()).stream().map(receiver -> {
                Target target = new Target();
                target.setAppId(appId);
                //使用ClientId
                //target.setClientId(receiver);
                //使用别名
                target.setAlias(receiver);
                return target;
            }).collect(Collectors.toList());
            String taskId = push.getContentId(message);
            result = push.pushMessageToList(taskId, targets);
        }
        String pushResult = MapUtils.getString(result.getResponse(), PUSH_RESULT_KEY);
        if (StringUtils.equalsIgnoreCase(pushResult, PUSH_SUCCESS)) {
            log.info("推送APP通知成功，返回信息：{}", result.getResponse().toString());
        } else {
            log.error("推送APP通知失败，返回信息：{}", result.getResponse().toString());
        }
    }

    /**
     * 根据通知类型设置通知内容
     * @param notification APP通知
     * @param message 个推通知消息
     */
    private void buildMessage(Notification notification, Message message) throws PushException, TemplateException, IOException, freemarker.template.TemplateException {
        switch (notification.getType()) {
            case NOTIFY:
                NotificationTemplate template = new NotificationTemplate();
                template.setAppId(appId);
                template.setAppkey(appKey);
                //广播等待客户端自启动
                template.setTransmissionType(2);
                Style0 style = new Style0();
                style.setTitle(notification.getTitle());
                style.setText(TemplateUtils.processToString(notification.getMsgTemplate().getTemplateDir(),
                        notification.getMsgTemplate().getTemplateFilename(), notification.getParams()));
                template.setStyle(style);
                message.setData(template);
                break;
            case LINK:
                LinkTemplate linkTemplate = new LinkTemplate();
                linkTemplate.setAppId(appId);
                linkTemplate.setAppkey(appKey);
                Style0 linkStyle = new Style0();
                linkStyle.setTitle(notification.getTitle());
                linkStyle.setText(TemplateUtils.processToString(notification.getMsgTemplate().getTemplateDir(),
                        notification.getMsgTemplate().getTemplateFilename(), notification.getParams()));
                linkTemplate.setStyle(linkStyle);
                linkTemplate.setUrl(notification.getUrl());
                message.setData(linkTemplate);
                break;
            default:
                throw new PushException(String.format("不支持的APP通知类型: %s", notification.getType()));
        }
        message.setOffline(true);
        message.setOfflineExpireTime(offlineExpireTime);
        message.setPushNetWorkType(0);
    }
}
