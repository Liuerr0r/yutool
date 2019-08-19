package com.yupaits.yutool.cache.config;

import com.yupaits.yutool.cache.core.CacheExecutor;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.cache.core.ICacheExecutor;
import com.yupaits.yutool.cache.core.LocalCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存自动装配
 * @author yupaits
 * @date 2019/7/16
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfigure {

    @Bean
    @Qualifier("cacheRedisTemplate")
    public RedisTemplate cacheRedisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        connectionFactory.setValidateConnection(true);
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cache", value = "enabled", havingValue = "true")
    public LocalCacheManager localCacheManager(CacheProperties cacheProperties) {
        return new LocalCacheManager(cacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cache", value = "enabled", havingValue = "true")
    public ICacheExecutor cacheExecutor(@Qualifier("cacheRedisTemplate") RedisTemplate redisTemplate, LocalCacheManager localCacheManager) {
        return new CacheExecutor(redisTemplate, localCacheManager);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "cache", value = "enabled", havingValue = "true")
    public CacheTemplate cacheTemplate(ICacheExecutor cacheExecutor) {
        return new CacheTemplate(cacheExecutor);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", value = "enabled", havingValue = "true")
    public CacheAspect cacheAspect(CacheProperties cacheProperties, CacheTemplate cacheTemplate) {
        return new CacheAspect(cacheProperties, cacheTemplate);
    }
}
