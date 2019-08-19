package com.yupaits.yutool.orm.base;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yupaits.yutool.commons.constant.LogConstants;
import com.yupaits.yutool.commons.exception.BusinessException;
import com.yupaits.yutool.commons.result.ResultCode;
import com.yupaits.yutool.commons.service.OptService;
import com.yupaits.yutool.orm.support.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ServiceImpl实现基类
 * @author yupaits
 * @date 2019/7/15
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<ID extends Serializable, T extends AbstractModel<ID, T>, M extends BaseMapper<T>> extends ServiceImpl<M, T> implements IBaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    private final Class<? extends AbstractModel> modelClass;
    private final OptService optService;
    private final AuditLogger auditLogger;
    private Class<? extends BaseVo> voClass;
    private VoBuilder voBuilder;
    private ModelBuilder modelBuilder;

    protected BaseServiceImpl(Class<? extends AbstractModel> modelClass, OptService optService, AuditLogger auditLogger) {
        this.modelClass = modelClass;
        this.optService = optService;
        this.auditLogger = auditLogger;
        setDefaultVoConfig();
        setDefaultModelBuilder();
    }

    /**
     * 根据ID获取Vo
     * @param id ID
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo getVoById(ID id) throws BusinessException {
        Vo vo = fromModel(getById(id), VoProps.defaultProps());
        clearVoConfig();
        return vo;
    }

    /**
     * 根据ID和VoProps获取Vo
     * @param id ID
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo getVoById(ID id, VoProps voProps) throws BusinessException {
        Vo vo = fromModel(getById(id), voProps);
        clearVoConfig();
        return vo;
    }

    /**
     * 根据QueryWrapper获取第一个Vo
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo getVo(Wrapper<T> queryWrapper) throws BusinessException {
        Vo vo = fromModel(getOne(queryWrapper), VoProps.defaultProps());
        clearVoConfig();
        return vo;
    }

    /**
     * 根据QueryWrapper和VoProps获取第一个Vo
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo getVo(Wrapper<T> queryWrapper, VoProps voProps) throws BusinessException {
        Vo vo = fromModel(getOne(queryWrapper), voProps);
        clearVoConfig();
        return vo;
    }

    /**
     * 根据ID集合获取Vo列表
     * @param ids ID集合
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listVoByIds(Collection<ID> ids) {
        return toVoList(listByIds(ids), VoProps.defaultProps());
    }

    /**
     * 根据ID集合和VoProps获取Vo列表
     * @param ids ID集合
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listVoByIds(Collection<ID> ids, VoProps voProps) {
        return toVoList(listByIds(ids), voProps);
    }

    /**
     * 获取所有Vo列表
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listAllVo() {
        return toVoList(list(), VoProps.defaultProps());
    }

    /**
     * 根据VoProps获取所有Vo列表
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listAllVo(VoProps voProps) {
        return toVoList(list(), voProps);
    }

    /**
     * 根据QueryWrapper获取Vo列表
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listVo(Wrapper<T> queryWrapper) {
        return toVoList(list(queryWrapper), VoProps.defaultProps());
    }

    /**
     * 根据QueryWrapper和VoProps获取Vo列表
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo列表
     */
    public <Vo extends BaseVo> List<Vo> listVo(Wrapper<T> queryWrapper, VoProps voProps) {
        return toVoList(list(queryWrapper), voProps);
    }

    /**
     * 根据IPage分页查询对象获取Vo分页数据
     * @param page 分页查询参数
     * @param <Vo> Vo类型
     * @return vo分页数据
     */
    public <Vo extends BaseVo> IPage<Vo> pageVo(IPage<T> page) {
        return toVoPage(page(page), VoProps.defaultProps());
    }

    /**
     * 根据IPage分页查询对象和VoProps获取Vo分页数据
     * @param page 分页查询参数
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo分页数据
     */
    public <Vo extends BaseVo> IPage<Vo> pageVo(IPage<T> page, VoProps voProps) {
        return toVoPage(page(page), voProps);
    }

    /**
     * 根据IPage分页查询对象和QueryWrapper获取Vo分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     * @return vo分页数据
     */
    public <Vo extends BaseVo> IPage<Vo> pageVo(IPage<T> page, Wrapper<T> queryWrapper) {
        return toVoPage(page(page, queryWrapper), VoProps.defaultProps());
    }

    /**
     * 根据IPage分页查询对象、QueryWrapper和VoProps获取Vo分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo分页数据
     */
    public <Vo extends BaseVo> IPage<Vo> pageVo(IPage<T> page, Wrapper<T> queryWrapper, VoProps voProps) {
        return toVoPage(page(page, queryWrapper), voProps);
    }

    /**
     * 保存Dto（ID不存在插入记录，ID存在更新记录）
     * @param dto dto对象
     * @param <Dto> Dto类型
     * @return 是否保存成功
     */
    public <Dto extends BaseDto> boolean saveDto(Dto dto) throws BusinessException {
        if (!dto.isValid()) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, dto);
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dto: %s", dto));
        }
        checkUnique(dto);
        boolean result = saveOrUpdate(modelFromDto(dto));
        ServiceContext.removeModelBuilder();
        return result;
    }

    /**
     * 批量保存Dto
     * @param dtos dto集合
     * @param <Dto> Dto类型
     * @return 是否批量保存成功
     */
    public <Dto extends BaseDto> boolean saveBatchDto(Collection<Dto> dtos) throws BusinessException {
        return saveBatchDto(dtos, 1000);
    }

    /**
     * 设置分批次保存记录数并批量保存Dto
     * @param dtos dto集合
     * @param batchSize 分批次记录数
     * @param <Dto> Dto类型
     * @return 是否批量保存成功
     */
    public <Dto extends BaseDto> boolean saveBatchDto(Collection<Dto> dtos, int batchSize) throws BusinessException {
        if (!BaseDto.isValid(dtos)) {
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dtos: %s", Arrays.toString(dtos.toArray())));
        }
        List<T> models = Lists.newArrayList();
        List<Dto> invalidDtos = Lists.newArrayList();
        //Dto集合自身进行唯一字段和组合唯一索引校验
        List<Dto> distinctSortedDtos = distinctAndSortDtos(dtos);
        invalidDtos.addAll(ListUtils.removeAll(dtos, distinctSortedDtos));
        for (Dto dto : distinctSortedDtos) {
            if (!dto.isValid()) {
                invalidDtos.add(dto);
                continue;
            }
            try {
                //对数据库中进行唯一字段和组合唯一索引校验
                checkUnique(dto);
                models.add(modelFromDto(dto));
            } catch (BusinessException e) {
                invalidDtos.add(dto);
            }
        }
        if (invalidDtos.size() > 0) {
            logger.warn(LogConstants.VALIDATE_OR_CONFLICT_FAIL_WITH_PARAM, Arrays.toString(invalidDtos.toArray()));
        }
        ServiceContext.removeModelBuilder();
        return saveOrUpdateBatch(models, batchSize);
    }

    /**
     * 根据QueryWrapper更新Dto
     * @param updateDto 更新dto对象
     * @param queryWrapper queryWrapper对象
     * @param <Dto> Dto类型
     * @return 是否更新成功
     * @throws BusinessException 抛出BusinessException
     */
    public <Dto extends BaseDto> boolean updateDto(Dto updateDto, @NonNull QueryWrapper<T> queryWrapper) throws BusinessException {
        if (!updateDto.isValid()) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, updateDto);
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dto: %s", updateDto));
        }
        //数据权限过滤
        queryWrapper.eq("id", updateDto.fetchId());
        if (getOne(queryWrapper) == null) {
            return false;
        }
        //更新数据
        checkUnique(updateDto);
        boolean result = updateById(modelFromDto(updateDto));
        ServiceContext.removeModelBuilder();
        return result;
    }

    /**
     * 根据ID集合统计数量
     * @param ids ID集合
     * @return 统计数量
     */
    public int count(Collection<ID> ids) {
        return count(new QueryWrapper<T>().in("id", ids));
    }

    /**
     * 根据IPage分页查询结果和AggregateProps获取携带聚合数据的分页信息
     * @param page 分页查询结果
     * @param queryWrapper queryWrapper查询对象
     * @param aggregateProps aggregateProps对象
     * @return 携带聚合数据的分页信息
     */
    public <Vo extends BaseVo> IAggregatePage<Vo> pageAggregate(IPage<Vo> page, @NonNull Wrapper<T> queryWrapper, AggregateProps aggregateProps) {
        AggregatePage<Vo> aggregatePage;
        if (page instanceof AggregatePage) {
            aggregatePage = (AggregatePage<Vo>) page;
        } else {
            aggregatePage = new AggregatePage(page);
        }
        if (CollectionUtils.isNotEmpty(aggregateProps.getAggregates())) {
            Set<AggregateField> aggregates = Sets.newHashSet(aggregateProps.getAggregates());
            List<AggregateResult> aggregateResults = Lists.newArrayListWithExpectedSize(aggregates.size());
            for (AggregateField aggregateField : aggregates) {
                QueryWrapper<T> query = ((QueryWrapper<T>) queryWrapper).clone();
                if (!ArrayUtils.contains(AggregateType.values(), aggregateField.getType())) {
                    logger.error("不支持的聚合类型: {}", aggregateField.getType());
                    continue;
                }
                switch (aggregateField.getType()) {
                    case SUM:
                        query.select(String.format("sum(%s)", aggregateField.getColumn()));
                        break;
                    case COUNT:
                        query.select(String.format("count(%s)", aggregateField.getColumn()));
                        break;
                    case AVERAGE:
                        query.select(String.format("average(%s)", aggregateField.getColumn()));
                        break;
                    case MAX:
                        query.select(String.format("max(%s)", aggregateField.getColumn()));
                        break;
                    case MIN:
                        query.select(String.format("min(%s)", aggregateField.getColumn()));
                        break;
                    default:
                }
                aggregateResults.add(new AggregateResult<>(aggregateField.getColumn(), aggregateField.getType(), getObj(query, value -> value)));
            }
            aggregatePage.setAggregates(aggregateResults);
        }
        return aggregatePage;
    }

    /**
     * 检查逻辑唯一字段和联合唯一索引的约束情况
     * @param dto dto对象
     * @param <Dto> Dto类型
     * @throws BusinessException 抛出BusinessException
     */
    protected <Dto extends BaseDto> void checkUnique(Dto dto) throws BusinessException {
        String[] uniqueFields = dto.uniqueFields();
        String[] unionKeyFieldNames = dto.unionKeyFields();
        if (ArrayUtils.isEmpty(uniqueFields) && ArrayUtils.isEmpty(unionKeyFieldNames)) {
            return;
        }
        Class<Dto> dtoClass = (Class<Dto>) dto.getClass();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (ArrayUtils.isNotEmpty(uniqueFields)) {
            for (String fieldName : uniqueFields) {
                if (StringUtils.isNotBlank(fieldName)) {
                    Field field = null;
                    boolean accessible = false;
                    try {
                        field = dtoClass.getDeclaredField(fieldName);
                        accessible = field.isAccessible();
                        field.setAccessible(true);
                        queryWrapper.eq(fieldName, field.get(dto));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        logger.warn(LogConstants.EXCEPTION_INFO, e);
                    } finally {
                        if (field != null) {
                            field.setAccessible(accessible);
                        }
                    }
                }
            }
        }
        if (ArrayUtils.isNotEmpty(unionKeyFieldNames)) {
            queryWrapper.or().nested(i -> {
                Arrays.stream(unionKeyFieldNames).forEach(fieldName -> {
                    if (StringUtils.isNotBlank(fieldName)) {
                        Field field = null;
                        boolean accessible = false;
                        try {
                            field = dtoClass.getDeclaredField(fieldName);
                            accessible = field.isAccessible();
                            field.setAccessible(true);
                            i.eq(fieldName, field.get(dto));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            logger.warn(LogConstants.EXCEPTION_INFO, e);
                        } finally {
                            if (field != null) {
                                field.setAccessible(accessible);
                            }
                        }
                    }
                });
                return i;
            });
        }
        T model = getOne(queryWrapper);
        //判断逻辑唯一字段和联合唯一索引是否冲突
        boolean isConflict = model != null && (dto.fetchId() == null || !dto.fetchId().equals(model.getId()));
        if (isConflict) {
            throw BusinessException.from(ResultCode.DATA_CONFLICT,
                    String.format("uniqueFields: %s, unionKey: %s, dto: %s", Arrays.toString(uniqueFields), Arrays.toString(unionKeyFieldNames), dto));
        }
    }

    /**
     * 根据逻辑唯一字段和联合唯一索引进行去重并对Dto集合进行排序
     * @param dtos Dto集合
     * @param <Dto> Dto类型
     * @return 去重排序后的集合
     */
    protected <Dto extends BaseDto> List<Dto> distinctAndSortDtos(Collection<Dto> dtos) {
        Class<Dto> dtoClass;
        List<Field> uniqueFields = Lists.newArrayList();
        List<Field> unionKeyFields = Lists.newArrayList();
        Comparator<Dto> comparator;
        Iterator<Dto> dtoIterator = dtos.iterator();
        //初始化 dtoClass 和 uniqueFields unionKeyFields comparator
        if (dtoIterator.hasNext()) {
            Dto dto = dtoIterator.next();
            dtoClass = (Class<Dto>) dto.getClass();
            String[] uniqueFieldNames = dto.uniqueFields();
            String[] unionKeyFieldNames = dto.unionKeyFields();
            comparator = dto.comparator();
            if (ArrayUtils.isNotEmpty(uniqueFieldNames)) {
                for (String fieldName : uniqueFieldNames) {
                    try {
                        uniqueFields.add(dtoClass.getDeclaredField(fieldName));
                    } catch (NoSuchFieldException e) {
                        logger.warn(LogConstants.EXCEPTION_INFO, e);
                    }
                }
            }
            if (ArrayUtils.isNotEmpty(unionKeyFieldNames)) {
                for (String fieldName : unionKeyFieldNames) {
                    try {
                        unionKeyFields.add(dtoClass.getDeclaredField(fieldName));
                    } catch (NoSuchFieldException e) {
                        logger.warn(LogConstants.EXCEPTION_INFO, e);
                    }
                }
            }
        } else {
            return Lists.newArrayList();
        }
        Map<String, Boolean> fieldAccessibleMap = uniqueFields.stream().collect(Collectors.toMap(Field::getName, Field::isAccessible));
        Map<String, Boolean> unionKeyFieldAccessibleMap = unionKeyFields.stream().collect(Collectors.toMap(Field::getName, Field::isAccessible));
        //利用TreeSet来进行去重
        Set<Dto> dtoSet = Sets.newTreeSet((dto1, dto2) -> {
            int result = 1;
            if (dto1.fetchId() != null && dto2.fetchId() != null && dto1.fetchId().equals(dto2.fetchId())) {
                return 0;
            }
            for (Field field : uniqueFields) {
                field.setAccessible(true);
                try {
                    if (field.get(dto1).equals(field.get(dto2))) {
                        return 0;
                    }
                } catch (IllegalAccessException e) {
                    logger.warn(LogConstants.EXCEPTION_INFO, e);
                } finally {
                    //还原属性的 accessible
                    field.setAccessible(fieldAccessibleMap.get(field.getName()));
                }
            }
            for (Field field : unionKeyFields) {
                field.setAccessible(true);
                try {
                    if (!field.get(dto1).equals(field.get(dto2))) {
                        break;
                    }
                } catch (IllegalAccessException e) {
                    logger.warn(LogConstants.EXCEPTION_INFO, e);
                } finally {
                    //还原属性的 accessible
                    field.setAccessible(unionKeyFieldAccessibleMap.get(field.getName()));
                }
            }
            return result;
        });
        dtoSet.addAll(dtos);
        List<Dto> distinctDtos = Lists.newArrayList(dtoSet);
        //根据自定义的规则进行排序
        if (comparator != null) {
            distinctDtos.sort(comparator);
        }
        return distinctDtos;
    }

    /**
     * 将Dto转换成待Save的Model
     * @param dto dto对象
     * @param <Dto> Dto类型
     * @return 待Save的Model
     * @throws BusinessException 抛出BusinessException
     */
    protected <Dto extends BaseDto> T modelFromDto(Dto dto) throws BusinessException {
        T model;
        if (dto.fetchId() != null) {
            //更新时，如果dto中未设置model的必填字段，则需要根据ID从数据库中查出待更新的实体以填充这些必填字段
            model = getById(dto.fetchId());
            if (model == null) {
                model = toModel(dto);
            } else {
                T sourceModel = newModel();
                BeanUtils.copyProperties(dto, sourceModel);
                if (ServiceContext.getModelBuilder() != null) {
                    ServiceContext.getModelBuilder().buildModel(sourceModel, dto);
                } else if (this.modelBuilder != null) {
                    this.modelBuilder.buildModel(sourceModel, dto);
                }
                //获取乐观锁
                Map<Field, Object> versions = fetchVersions(model);
                //审计日志
                auditLogger.auditLog(AuditEvent.builder().source(sourceModel).target(model).build());
                //填充乐观锁
                fillVersion(model, versions);
            }
        } else {
            model = toModel(dto);
        }
        return model;
    }

    /**
     * Dto转成Model
     * @param dto dto对象
     * @param <Dto> Dto类型
     * @return model对象
     */
    public <Dto extends BaseDto> T toModel(Dto dto) throws BusinessException {
        T model = newModel();
        BeanUtils.copyProperties(dto, model);
        model.setId((ID) dto.fetchId());
        if (ServiceContext.getModelBuilder() != null) {
            ServiceContext.getModelBuilder().buildModel(model, dto);
        } else if (this.modelBuilder != null) {
            this.modelBuilder.buildModel(model, dto);
        }
        return model;
    }

    /**
     * Model转成Vo
     * @param model model对象
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo fromModel(T model) throws BusinessException {
        return fromModel(model, VoProps.defaultProps());
    }

    /**
     * 根据VoProps将Model转成Vo
     * @param model model对象
     * @param voProps VoProps配置
     * @param <Vo> Vo类型
     * @return vo对象
     */
    public <Vo extends BaseVo> Vo fromModel(T model, VoProps voProps) throws BusinessException {
        if (model == null) {
            return null;
        }
        Vo vo = newVo();
        BeanUtils.copyProperties(model, vo);
        vo.setId(model.getId());
        vo.setCreatedTime(voProps.isWithCreatedTime() ? model.getCreatedTime() : null);
        vo.setCreatedBy(voProps.isWithCreatedBy() ? model.getCreatedBy() : null);
        vo.setLastModifiedTime(voProps.isWithLastModifiedTime() ? model.getLastModifiedTime() : null);
        vo.setLastModifiedBy(voProps.isWithLastModifiedBy() ? model.getLastModifiedBy() : null);
        if (optService != null) {
            vo.setCreatedByName(voProps.isWithCreatedByName() ? optService.getOptName(model.getCreatedBy()) : null);
            vo.setLastModifiedByName(voProps.isWithLastModifiedByName() ? optService.getOptName(model.getLastModifiedBy()) : null);
        }
        if (ServiceContext.getVoBuilder() != null) {
            ServiceContext.getVoBuilder().buildVo(vo, model);
        } else if (this.voBuilder != null) {
            this.voBuilder.buildVo(vo, model);
        }
        return vo;
    }

    /**
     * 创建Model对象
     * @return model对象
     * @throws BusinessException 抛出BusinessException
     */
    private T newModel() throws BusinessException {
        try {
            return (T) modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw BusinessException.from(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 创建Vo对象
     * @param <Vo> Vo类型
     * @return vo对象
     * @throws BusinessException 抛出BusinessException
     */
    private <Vo extends BaseVo> Vo newVo() throws BusinessException {
        try {
            Class<? extends BaseVo> voClass = ServiceContext.getVoClass() != null ? ServiceContext.getVoClass() : this.voClass;
            return (Vo) voClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw BusinessException.from(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 设置默认的VoClass
     * @param voClass VoClass对象
     */
    protected void setDefaultVoClass(Class<? extends BaseVo> voClass) {
        this.voClass = voClass;
    }

    /**
     * 设置默认的VoBuilder
     * @param voBuilder VoBuilder对象
     */
    protected void setDefaultVoBuilder(VoBuilder voBuilder) {
        this.voBuilder = voBuilder;
    }

    /**
     * 设置默认的ModelBuilder
     * @param modelBuilder ModelBuilder对象
     */
    protected void setDefaultModelBuilder(ModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;
    }

    /**
     * 设置VoClass，用于动态替换默认的VoClass类型
     * @param voClass voClass对象
     * @param <Vo> Vo类型
     */
    @Override
    public <Vo extends BaseVo> void setVoClass(Class<Vo> voClass) {
        ServiceContext.setVoClass(voClass);
    }

    /**
     * 设置ModelBuilder，用于动态替换默认的ModelBuilder
     * @param modelBuilder modelBuilder对象
     */
    @Override
    public void setModelBuilder(ModelBuilder modelBuilder) {
        ServiceContext.setModelBuilder(modelBuilder);
    }

    /**
     * 设置VoBuilder，用于动态替换默认的VoBuilder
     * @param voBuilder voBuilder对象
     */
    @Override
    public void setVoBuilder(VoBuilder voBuilder) {
        ServiceContext.setVoBuilder(voBuilder);
    }

    /**
     * Model列表转换成Vo列表
     * @param list model列表
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     * @return vo列表
     */
    private <Vo extends BaseVo> List<Vo> toVoList(Collection<T> list, VoProps voProps) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<Vo> voList = list.stream().map(model -> {
                Vo vo = null;
                try {
                    vo = fromModel(model, voProps);
                } catch (BusinessException e) {
                    logger.error(LogConstants.EXCEPTION_INFO, e);
                }
                return vo;
            }).collect(Collectors.toList());
            clearVoConfig();
            return voList;
        }
        clearVoConfig();
        return Lists.newArrayList();
    }

    /**
     * Model分页数据转成Vo分页数据
     * @param page Model分页数据
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     * @return Vo分页数据
     */
    private <Vo extends BaseVo> IPage<Vo> toVoPage(IPage<T> page, VoProps voProps) {
        IPage<Vo> voPage = page.convert(model -> {
            Vo vo = null;
            try {
                vo = fromModel(model, voProps);
            } catch (BusinessException e) {
                logger.error(LogConstants.EXCEPTION_INFO, e);
            }
            return vo;
        });
        clearVoConfig();
        return voPage;
    }

    /**
     * 获取Model乐观锁Map
     * @param model Model对象
     * @return 乐观锁Map
     */
    private Map<Field, Object> fetchVersions(T model) {
        Map<Field, Object> versions = Maps.newHashMap();
        Arrays.stream(model.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Version.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        versions.put(field, field.get(model));
                    } catch (IllegalAccessException e) {
                        logger.warn("获取乐观锁值失败，类型：{}，字段名：{}", model.getClass().getCanonicalName(), field.getName(), e);
                    }
                });
        return versions;
    }

    /**
     * 设置乐观锁字段
     * @param model 待更新Model
     * @param versions 乐观锁
     */
    private void fillVersion(T model, Map<Field, Object> versions) {
        if (MapUtils.isNotEmpty(versions)) {
            versions.forEach((field, value) -> {
                try {
                    field.set(model, value);
                } catch (IllegalAccessException e) {
                    logger.warn("设置乐观锁值失败，类型：{}，字段名：{}", model.getClass().getCanonicalName(), field.getName(), e);
                }
            });
        }
    }

    /**
     * 清除Vo设置
     */
    protected void clearVoConfig() {
        ServiceContext.removeVoClass();
        ServiceContext.removeVoBuilder();
    }
}
