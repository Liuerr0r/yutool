package com.yupaits.yutool.cache.config;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.yupaits.yutool.cache.annotation.EnableCache;
import com.yupaits.yutool.cache.core.CacheTemplate;
import com.yupaits.yutool.cache.core.NullValue;
import com.yupaits.yutool.cache.exception.CacheException;
import com.yupaits.yutool.cache.support.CacheProps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存切面
 * @author yupaits
 * @date 2019/7/18
 */
@Slf4j
@Aspect
public class CacheAspect {
    private static final String KEY_DELIMITER = ":";

    private static final String ADVICES = "execution(!final * com.yupaits..*.*(..)) " +
            "&& !@annotation(com.yupaits.yutool.cache.annotation.DisableCache) " +
            "&& !@target(com.yupaits.yutool.cache.annotation.DisableCache) " +
            "&& (@annotation(com.yupaits.yutool.cache.annotation.EnableCache) " +
            "|| @target(com.yupaits.yutool.cache.annotation.EnableCache))";

    /**
     * 缓存设置Advices
     */
    private static final String FETCH_CACHE_ADVICES = ADVICES + " && !@annotation(com.yupaits.yutool.cache.annotation.EvictCache)";

    /**
     * 缓存失效Advices
     */
    private static final String EVICT_CACHE_ADVICES = ADVICES + " && @annotation(com.yupaits.yutool.cache.annotation.EvictCache)";

    /**
     * BloomFilter用于解决缓存穿透，实际使用时最好先进行缓存预热
     */
    @SuppressWarnings("UnstableApiUsage")
    private final BloomFilter<String> cacheKeyFilter;

    /**
     * 线程锁用于解决缓存雪崩
     */
    private final ReentrantLock lock = new ReentrantLock();

    private final CacheProperties cacheProperties;
    private final CacheTemplate cacheTemplate;

    @SuppressWarnings("UnstableApiUsage")
    @Autowired
    public CacheAspect(CacheProperties cacheProperties, CacheTemplate cacheTemplate) {
        this.cacheProperties = cacheProperties;
        this.cacheTemplate = cacheTemplate;
        this.cacheKeyFilter = cacheProperties.isBloomFilterEnabled() ? BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 1000000L) : null;
    }

    @Around(FETCH_CACHE_ADVICES)
    public Object getCache(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!cacheProperties.isEnabled()) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] params = joinPoint.getArgs();
        //生成缓存key
        String key = cacheKey(className, methodName, params);
        if (cacheProperties.isBloomFilterEnabled()) {
            //布隆过滤，判断key是否已经存在
            if (!cacheKeyFilter.mightContain(key)) {
                //key不存在直接返回null
                cacheKeyFilter.put(key);
                return null;
            }
        }
        //根据注解装配CacheProps参数，优先级：方法注解 > 类注解
        EnableCache enableCache = null;
        if (((MethodSignature) joinPoint.getSignature()).getMethod().isAnnotationPresent(EnableCache.class)) {
            enableCache = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(EnableCache.class);
        } else if (joinPoint.getTarget().getClass().isAnnotationPresent(EnableCache.class)) {
            enableCache = joinPoint.getTarget().getClass().getAnnotation(EnableCache.class);
        }
        CacheProps cacheProps = fromCacheAnnotation(enableCache);
        //获取缓存数据
        Object value = cacheTemplate.getCache(key, cacheProps);
        Object result;
        if (null == value) {
            //缓存未命中
            //执行查询操作，加锁
            lock.lock();
            try {
                result = joinPoint.proceed(params);
            } finally {
                //释放锁
                lock.unlock();
            }
            if (result == null) {
                //设置缓存空值
                cacheTemplate.setCache(key, new NullValue(), cacheProps);
            } else {
                cacheTemplate.setCache(key, result, cacheProps);
            }
            if (log.isDebugEnabled()) {
                log.debug("耗时: {}ms，设置缓存[{}]", System.currentTimeMillis() - startTime, key);
            }
        } else {
            //缓存命中
            if (value instanceof NullValue) {
                //如果缓存的是空值则设置结果为null
                result = null;
            } else {
                result = value;
            }
            if (log.isDebugEnabled()) {
                log.debug("耗时: {}ms，命中缓存[{}]", System.currentTimeMillis() - startTime, key);
            }
        }
        return result;
    }

    @Around(EVICT_CACHE_ADVICES)
    public Object evictCache(ProceedingJoinPoint joinPoint) throws Throwable {
        if (cacheProperties.isEnabled()) {
            String key = cacheStoreKey(joinPoint.getSignature().getDeclaringTypeName());
            cacheTemplate.removeCacheByPrefix(key);
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    /**
     * 生成缓存仓库Key
     */
    private String cacheStoreKey(String className) {
        return cacheProperties.getKeyPrefix() + KEY_DELIMITER + className;
    }

    /**
     * 生成缓存Key
     */
    private String cacheKey(String className, String methodName, Object[] params) {
        StringBuilder builder = new StringBuilder(cacheStoreKey(className)).append(KEY_DELIMITER).append(methodName);
        for (Object param : params) {
            if (param != null) {
                builder.append(KEY_DELIMITER).append(param.toString());
            }
        }
        return builder.toString();
    }

    /**
     * 根据EnableCache注解参数生成CacheProps
     */
    private CacheProps fromCacheAnnotation(EnableCache enableCache) throws CacheException {
        if (enableCache == null) {
            return CacheProps.defaultCacheProps();
        }
        CacheProps cacheProps = CacheProps.builder()
                .cacheLocal(enableCache.local())
                .cacheDistribute(enableCache.distribute())
                .expired(enableCache.expired())
                .timeout(enableCache.timeout())
                .timeUnit(enableCache.timeUnit())
                .keyType(enableCache.keyType())
                .keyStrategy(enableCache.keyStrategy())
                .valueType(enableCache.valueType())
                .build();
        if (!cacheProps.isValid()) {
            throw new CacheException(String.format("缓存配置校验失败，配置信息：%s", cacheProps));
        }
        return cacheProps;
    }
}
