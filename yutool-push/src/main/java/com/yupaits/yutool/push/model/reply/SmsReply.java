package com.yupaits.yutool.push.model.reply;

import cn.hutool.core.util.XmlUtil;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * 短信消息回复
 * @author yupaits
 * @date 2019/7/19
 */
@Data
@Accessors(chain = true)
public class SmsReply implements IReply {
    private static final long serialVersionUID = 1L;

    /**
     * xml字符串
     */
    private String xmlString;

    /**
     * 短信类型
     * 1-发出 2-回复
     */
    private Integer type;

    /**
     * 短信数量
     */
    private Integer count;

    /**
     * 回复列表
     */
    private List<PushForm> list;

    /**
     * 通过xml组装list属性内容
     */
    public void composeList() {
        if (this.count != null && this.count > 0) {
            this.list = Lists.newArrayListWithExpectedSize(count);
            String xpath = StringUtils.EMPTY;
            if (type == 1) {
                xpath = "list/pushStatusForm";
            } else if (type == 2) {
                xpath = "list/pushSmsForm";
            }
            List<Element> elements = XmlUtil.transElements(XmlUtil.getNodeListByXPath(xpath, XmlUtil.parseXml(xmlString).getDocumentElement()));
            for (int i = 0; i < count; i++) {
                Element element = elements.get(i);
                this.list.add(new PushForm.PushFormBuilder()
                        .msgId(XmlUtil.elementText(element, "msgId"))
                        .status(XmlUtil.elementText(element, "status"))
                        .content(XmlUtil.elementText(element, "content"))
                        .userId(XmlUtil.elementText(element, "userId"))
                        .eprId(XmlUtil.elementText(element, "eprId"))
                        .mobile(XmlUtil.elementText(element, "mobile"))
                        .build());
            }
        }
    }

    /**
     * 校验参数
     * @return 校验通过
     */
    public boolean isValid() {
        return type != null && count != null && (count <= 0 || CollectionUtils.isNotEmpty(list));
    }

    /**
     * 根据xml字符串构建SmsReply对象
     * @param xmlString xml字符串
     * @return SmsReply对象
     */
    public static SmsReply fromXml(String xmlString) {
        Element rootElement = XmlUtil.parseXml(xmlString).getDocumentElement();
        SmsReply smsReply = new SmsReply()
                .setXmlString(xmlString)
                .setType(Integer.valueOf(XmlUtil.elementText(rootElement, "type")))
                .setCount(Integer.valueOf(XmlUtil.elementText(rootElement, "count")));
        smsReply.composeList();
        return smsReply;
    }

    @Data
    @Builder
    public static class PushForm {
        /**
         * 短信消息唯一标识
         */
        private String msgId;

        /**
         * 状态
         * -1 失败；1 成功
         */
        private String status;

        /**
         * 短信回复内容
         */
        private String content;

        /**
         * 用户ID
         */
        private String userId;

        /**
         * 企业ID
         */
        private String eprId;

        /**
         * 手机号码
         * type=1，为接收者手机号码；type=2，为发送者手机号码
         */
        private String mobile;
    }
}
