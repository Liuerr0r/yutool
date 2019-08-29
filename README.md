# yutool

![version](https://img.shields.io/badge/version-1.0.0-blue.svg)

## Links

[[使用文档](https://github.com/yupaits/yutool/wiki)][[示例代码](yutool-samples/README.md)]

## Features

**yutool** 是基于通用业务总结的一套业务工具和业务中间件，包含以下内容：

- [x] yutool-orm：基于mybatis-plus的包含通用DTO、VO相关的CRUD功能封装的ORM组件
- [x] yutool-mq：实现了延迟队列和重试队列的MQ组件
- [x] yutool-cache：本地和分布式缓存整合的缓存工具，基于注解的缓存切面
- [x] yutool-push：包含多种消息推送方式的统一推送接口，可以简单拓展具体的推送实现
- [x] yutool-file-server：基于fastdfs的分布式文件服务
- [x] yutool-template：代码模板工具

**yutool-plugins** yutool插件，目前有以下插件：

- [x] swagger-support：Swagger接口文档接入
- [x] audit-logger：基于注解的审计记录工具
- [x] jwt-helper：JWT辅助工具
- [x] sms-verify：开箱即用的短信验证码工具

## Getting Started

- Maven示例
    
    在 `pom.xml` 中添加依赖：

    ```xml
    <parent>
        <groupId>com.yutool</groupId>
        <artifactId>yutool-parent</artifactId>
        <version>${yutool.version}</version>
        <relativePath/>
    </parent>

    <dependencies>
        <dependency>
            <groupId>com.yupaits</groupId>
            <artifactId>yutool-orm</artifactId>
        </dependency>
    </dependencies>
    ```

    各个组件的使用文档详见：[使用文档](https://github.com/yupaits/yutool/wiki)

## License

[MIT License](https://github.com/yupaits/yutool/blob/master/LICENSE)