package com.yupaits.yutool.plugin.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.service.Contact;

import java.util.Collection;

/**
 * API分组信息
 * @author yupaits
 * @date 2019/7/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupApiInfo {

    /**
     * 分组名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口所在包路径
     */
    private String basePackage;

    /**
     * 联系人名字
     */
    private String contactName;

    /**
     * 联系人网址
     */
    private String contactUrl;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 获取联系人信息
     * @return 联系人信息
     */
    public Contact getContact() {
        if (StringUtils.isAllBlank(contactName, contactUrl, contactEmail)) {
            return null;
        }
        return new Contact(contactName, contactUrl, contactEmail);
    }

    /**
     * 参数校验
     * @return 校验通过
     */
    public boolean isValid() {
        return !StringUtils.isAnyBlank(name, title, description, basePackage);
    }

    /**
     * 集合参数校验
     * @param groupApiInfos 集合
     * @return 校验通过
     */
    public static boolean isValid(Collection<GroupApiInfo> groupApiInfos) {
        if (CollectionUtils.isEmpty(groupApiInfos)) {
            return true;
        }
        for (GroupApiInfo groupApiInfo : groupApiInfos) {
            if (!groupApiInfo.isValid()) {
                return false;
            }
        }
        return true;
    }
}
