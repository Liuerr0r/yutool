package com.yupaits.yutool.plugin.swagger;

import com.yupaits.yutool.plugin.swagger.model.GroupApiInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.service.Contact;

import java.util.List;

/**
 * API文档配置
 * @author yupaits
 * @date 2019/7/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiProps {

    /**
     * 是否启用Swagger接口文档
     */
    private boolean enabled;

    /**
     * 版本号
     */
    private String version;

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
     * 服务条款网址
     */
    private String termsOfServiceUrl;

    /**
     * 许可证
     */
    private String license;

    /**
     * 许可证网址
     */
    private String licenseUrl;

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
     * 分组信息列表
     */
    private List<GroupApiInfo> groups;

    /**
     * 获取全局联系人信息
     * @return 全局联系人信息
     */
    public Contact getContact() {
        return new Contact(contactName, contactUrl, contactEmail);
    }

    /**
     * 参数校验
     * @return 校验通过
     */
    public boolean isValid() {
        return !StringUtils.isAnyBlank(version, contactName, contactUrl, contactEmail) && GroupApiInfo.isValid(groups)
                && (CollectionUtils.isEmpty(groups) || StringUtils.isNotBlank(basePackage));
    }
}
