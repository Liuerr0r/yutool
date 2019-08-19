package com.yupaits.yutool.plugin.swagger;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yupaits.yutool.plugin.swagger.model.GroupApiInfo;
import com.yupaits.yutool.plugin.swagger.utils.SwaggerUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Docket Bean注册
 * @author yupaits
 * @date 2019/7/30
 * @date 2019/8/1 使用BeanDefinitionRegistryPostProcessor根据配置文件自动注入分组Docket暂时无法实现，需要手动注册Docket Bean
 */
@Deprecated
public class DocketRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final String API_GROUPS_INDEX_PREFIX = "api.groups[";
    private static final String API_GROUPS_INDEX_FLAG = "]";
    private static final String API_GROUPS_PREFIX = "].";
    private static final String API_BEAN_SUFFIX = "Api";
    private static final String API_BEAN_NAME = "api";

    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ApiProps apiProps = loadApiProps(environment);
        checkApiProperties(apiProps);
        if (!apiProps.isEnabled()) {
            return;
        }
        //使用BeanDefinitionBuilder设置构造方法参数
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class)
                .addConstructorArgValue(DocumentationType.SWAGGER_2);
        List<GroupApiInfo> groups = apiProps.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            for (GroupApiInfo group : groups) {
                //注册分组Docket Bean
                String beanName = group.getName() + API_BEAN_SUFFIX;
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(builder.getBeanDefinition(), beanName);
                BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
            }
        } else {
            //注册Docket Bean
            BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(builder.getBeanDefinition(), API_BEAN_NAME);
            BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ApiProps apiProps = loadApiProps(environment);
        checkApiProperties(apiProps);
        if (!apiProps.isEnabled()) {
            return;
        }
        List<GroupApiInfo> groups = apiProps.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            for (GroupApiInfo group : groups) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(group.getName() + API_BEAN_SUFFIX);
                MutablePropertyValues values = beanDefinition.getPropertyValues();
                Docket docket = new Docket(null)
                        .groupName(group.getName())
                        .apiInfo(SwaggerUtils.apiInfo(apiProps, group))
                        .select()
                        .apis(RequestHandlerSelectors.basePackage(group.getBasePackage()))
                        .apis(Predicates.not(RequestHandlerSelectors.basePackage(SwaggerUtils.SPRING_BOOT_PACKAGE)))
                        .paths(PathSelectors.any())
                        .build();
                setPropertyValues(values, docket);
            }
        } else {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(API_BEAN_NAME);
            MutablePropertyValues values = beanDefinition.getPropertyValues();
            GroupApiInfo group = GroupApiInfo.builder()
                    .name(apiProps.getTitle())
                    .title(apiProps.getTitle())
                    .description(apiProps.getDescription())
                    .basePackage(apiProps.getBasePackage())
                    .contactName(apiProps.getContactName())
                    .contactUrl(apiProps.getContactUrl())
                    .contactEmail(apiProps.getContactEmail())
                    .build();
            Docket docket = new Docket(null)
                    .groupName(group.getName())
                    .apiInfo(SwaggerUtils.apiInfo(apiProps, group))
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(group.getBasePackage()))
                    .apis(Predicates.not(RequestHandlerSelectors.basePackage(SwaggerUtils.SPRING_BOOT_PACKAGE)))
                    .paths(PathSelectors.any())
                    .build();
            setPropertyValues(values, docket);
        }
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    /**
     * 加载APiProps配置信息
     * @param environment Environment对象
     * @return ApiProps
     */
    private ApiProps loadApiProps(Environment environment) {
        StandardEnvironment env = (StandardEnvironment) environment;
        ApiProps apiProps = ApiProps.builder()
                .enabled(env.getProperty("api.enabled", Boolean.class, false))
                .version(env.getProperty("api.version"))
                .contactName(env.getProperty("api.contactName"))
                .contactUrl(env.getProperty("api.contactUrl"))
                .contactEmail(env.getProperty("api.contactEmail"))
                .termsOfServiceUrl(env.getProperty("api.termsOfServiceUrl"))
                .license(env.getProperty("api.license"))
                .licenseUrl(env.getProperty("api.licenseUrl"))
                .title(env.getProperty("api.title"))
                .description(env.getProperty("api.description"))
                .basePackage(env.getProperty("api.basePackage"))
                .build();
        MutablePropertySources sources = env.getPropertySources();
        Iterator<PropertySource<?>> sourceIterator = sources.iterator();
        Map<Integer, GroupApiInfo> groupMap = Maps.newHashMap();
        while (sourceIterator.hasNext()) {
            PropertySource propertySource = sourceIterator.next();
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                Map<String, Object> propertyMap = ((OriginTrackedMapPropertySource) propertySource).getSource();
                for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
                    String key = entry.getKey();
                    if (StringUtils.startsWithIgnoreCase(key, API_GROUPS_INDEX_PREFIX)) {
                        Integer index = Integer.parseInt(StringUtils.substringBetween(key, API_GROUPS_INDEX_PREFIX, API_GROUPS_INDEX_FLAG));
                        String fieldName = StringUtils.substringAfter(key, API_GROUPS_PREFIX);
                        GroupApiInfo groupApiInfo;
                        if (groupMap.containsKey(index)) {
                            groupApiInfo = groupMap.get(index);
                            if (groupApiInfo == null) {
                                groupApiInfo = new GroupApiInfo();
                            }
                        } else {
                            groupApiInfo = new GroupApiInfo();
                        }
                        setGroupApiInfo(groupApiInfo, fieldName, entry.getValue());
                        groupMap.put(index, groupApiInfo);
                    }
                }
            }
        }
        //将GroupApiInfo信息按index排序
        SortedMap<Integer, GroupApiInfo> sortedGroupMap = Maps.newTreeMap();
        sortedGroupMap.putAll(groupMap);
        List<GroupApiInfo> groups = Lists.newArrayListWithExpectedSize(sortedGroupMap.size());
        sortedGroupMap.forEach((index, value) -> groups.add(value));
        apiProps.setGroups(groups);
        return apiProps;
    }

    /**
     * 填充GroupApiInfo对象
     * @param groupApiInfo GroupApiInfo对象
     * @param fieldName 属性名称
     * @param value 待填充的属性值
     */
    private void setGroupApiInfo(@NonNull GroupApiInfo groupApiInfo, String fieldName, Object value) {
        switch (fieldName) {
            case "name":
                groupApiInfo.setName(String.valueOf(value));
                break;
            case "title":
                groupApiInfo.setTitle(String.valueOf(value));
                break;
            case "description":
                groupApiInfo.setDescription(String.valueOf(value));
                break;
            case "basePackage":
                groupApiInfo.setBasePackage(String.valueOf(value));
                break;
            case "contactName":
                groupApiInfo.setContactName(String.valueOf(value));
                break;
            case "contactUrl":
                groupApiInfo.setContactUrl(String.valueOf(value));
                break;
            case "contactEmail":
                groupApiInfo.setContactEmail(String.valueOf(value));
                break;
            default:
        }
    }

    /**
     * 校验ApiProperties
     * @param apiProps ApiProperties参数
     */
    private void checkApiProperties(ApiProps apiProps) {
        if (apiProps == null || !apiProps.isValid()) {
            throw new BeanDefinitionValidationException(String.format("接口文档配置参数校验失败，参数：%s", apiProps));
        }
    }

    /**
     * 设置Bean对象的属性
     * @param values MutablePropertyValues
     * @param docket Docket对象
     */
    private void setPropertyValues(MutablePropertyValues values, Docket docket) {
        values.addPropertyValue("groupName", docket.getGroupName());
        values.addPropertyValue("apiInfo", apiInfo(docket));
        values.addPropertyValue("apiSelector", apiSelector(docket));
    }

    /**
     * 反射获取Docket中的ApiInfo
     * @param docket Docket
     * @return ApiInfo
     */
    private ApiInfo apiInfo(Docket docket) {
        Field apiInfoField = null;
        try {
            apiInfoField = Docket.class.getDeclaredField("apiInfo");
            apiInfoField.setAccessible(true);
            return (ApiInfo) apiInfoField.get(docket);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return ApiInfo.DEFAULT;
        } finally {
            if (apiInfoField != null) {
                apiInfoField.setAccessible(false);
            }
        }
    }

    /**
     * 反射获取Docket中的ApiSelector
     * @param docket Docket
     * @return ApiSelector
     */
    private ApiSelector apiSelector(Docket docket) {
        Field apiSelectorField = null;
        try {
            apiSelectorField = Docket.class.getDeclaredField("apiSelector");
            apiSelectorField.setAccessible(true);
            return (ApiSelector) apiSelectorField.get(docket);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return ApiSelector.DEFAULT;
        } finally {
            if (apiSelectorField != null) {
                apiSelectorField.setAccessible(false);
            }
        }
    }
}