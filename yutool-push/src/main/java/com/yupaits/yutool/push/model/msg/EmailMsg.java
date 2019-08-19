package com.yupaits.yutool.push.model.msg;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yupaits.yutool.push.support.IMsgTemplate;
import com.yupaits.yutool.push.support.PushProps;
import com.yupaits.yutool.push.support.PushType;
import com.yupaits.yutool.template.exception.TemplateException;
import com.yupaits.yutool.template.utils.TemplateUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 邮件消息
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class EmailMsg implements IMsg {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件模板
     */
    private IMsgTemplate msgTemplate;

    /**
     * 邮件模板参数
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 发件人
     */
    private String from;

    /**
     * 邮件主题
     */
    private String subject;

    @Override
    public boolean isValid() {
        return msgTemplate != null && !StringUtils.isAnyBlank(from, subject);
    }

    @Override
    public EmailMsg putParam(String key, Object value) {
        if (this.params == null) {
            this.params = Maps.newHashMap();
        }
        this.params.put(key, value);
        return this;
    }

    @Override
    public EmailMsg removeParam(String key) {
        if (this.params != null) {
            this.params.remove(key);
        }
        return this;
    }

    /**
     * 根据PushProps参数生成SimpleMailMessage邮件对象
     * @param pushProps PushProps参数
     * @return SimpleMailMessage邮件对象
     * @throws TemplateException 抛出TemplateException
     * @throws IOException 抛出IOException
     * @throws freemarker.template.TemplateException 抛出freemarker的TemplateException
     */
    public SimpleMailMessage from(PushProps pushProps) throws TemplateException, IOException, freemarker.template.TemplateException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.setTo(pushProps.getReceivers().getOrDefault(PushType.EMAIL, Lists.newArrayList()).toArray(new String[0]));
        if (!CollectionUtils.isEmpty(pushProps.getCc())) {
            mail.setCc(pushProps.getCc().toArray(new String[0]));
        }
        if (!CollectionUtils.isEmpty(pushProps.getBcc())) {
            mail.setBcc(pushProps.getBcc().toArray(new String[0]));
        }
        mail.setText(TemplateUtils.processToString(msgTemplate.getTemplateDir(), msgTemplate.getTemplateFilename(), params));
        return mail;
    }
}
