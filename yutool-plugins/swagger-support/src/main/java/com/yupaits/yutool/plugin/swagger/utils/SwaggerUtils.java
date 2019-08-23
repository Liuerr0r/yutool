package com.yupaits.yutool.plugin.swagger.utils;

import com.google.common.base.Predicates;
import com.yupaits.yutool.plugin.swagger.config.ApiProps;
import com.yupaits.yutool.plugin.swagger.model.GroupApiInfo;
import org.apache.commons.lang3.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger工具类
 * @author yupaits
 * @date 2019/8/1
 */
public class SwaggerUtils {
    public static final String SPRING_BOOT_PACKAGE = "org.springframework.boot";

    /**
     * 获取全局Docket
     * @param apiProps ApiProps参数
     * @return Docket对象
     */
    public static Docket docket(ApiProps apiProps) {
        GroupApiInfo group = GroupApiInfo.builder()
                .name(Docket.DEFAULT_GROUP_NAME)
                .title(apiProps.getTitle())
                .description(apiProps.getDescription())
                .basePackage(apiProps.getBasePackage())
                .contactName(apiProps.getContactName())
                .contactUrl(apiProps.getContactUrl())
                .contactEmail(apiProps.getContactEmail())
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(group.getName())
                .apiInfo(apiInfo(apiProps, group))
                .select()
                .apis(RequestHandlerSelectors.basePackage(group.getBasePackage()))
                .apis(Predicates.not(RequestHandlerSelectors.basePackage(SwaggerUtils.SPRING_BOOT_PACKAGE)))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 根据分组名获取Docket
     * @param apiProps ApiProps参数
     * @param groupName 分组名
     * @return Docket对象
     */
    public static Docket docket(ApiProps apiProps, String groupName) {
        GroupApiInfo group = apiProps.getGroups().stream()
                .filter(info -> StringUtils.equals(groupName, info.getName()))
                .findFirst()
                .orElse(new GroupApiInfo());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(group.getName())
                .apiInfo(apiInfo(apiProps, group))
                .select()
                .apis(RequestHandlerSelectors.basePackage(group.getBasePackage()))
                .apis(Predicates.not(RequestHandlerSelectors.basePackage(SPRING_BOOT_PACKAGE)))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 获取ApiInfo信息
     * @param apiProps ApiProperties配置
     * @param groupApiInfo 分组API信息
     * @return ApiInfo对象
     */
    public static ApiInfo apiInfo(ApiProps apiProps, GroupApiInfo groupApiInfo) {
        return new ApiInfoBuilder().title(groupApiInfo.getTitle())
                .description(groupApiInfo.getDescription())
                .version(apiProps.getVersion())
                .contact(groupApiInfo.getContact() != null ? groupApiInfo.getContact() : apiProps.getContact())
                .license(apiProps.getLicense())
                .licenseUrl(apiProps.getLicenseUrl())
                .termsOfServiceUrl(apiProps.getTermsOfServiceUrl())
                .build();
    }
}
