package com.yupaits.yutool.orm.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.yupaits.yutool.commons.constant.LogConstants;
import com.yupaits.yutool.commons.exception.BusinessException;
import com.yupaits.yutool.commons.result.Result;
import com.yupaits.yutool.commons.result.ResultCode;
import com.yupaits.yutool.commons.result.ResultWrapper;
import com.yupaits.yutool.commons.service.OptService;
import com.yupaits.yutool.orm.support.AggregateProps;
import com.yupaits.yutool.orm.support.AuditLogger;
import com.yupaits.yutool.orm.support.ServiceContext;
import com.yupaits.yutool.orm.support.VoProps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yupaits
 * @date 2019/7/15
 */
public abstract class BaseResultServiceImpl<ID extends Serializable, T extends AbstractModel<ID, T>, M extends BaseMapper<T>> extends BaseServiceImpl<ID, T, M> {
    private static final Logger logger = LoggerFactory.getLogger(BaseResultServiceImpl.class);

    protected BaseResultServiceImpl(Class<? extends AbstractModel> modelClass, OptService optService, AuditLogger auditLogger) {
        super(modelClass, optService, auditLogger);
    }

    /**
     * 根据ID获取记录
     * @param id ID
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<Vo> resultById(ID id) throws BusinessException {
        return ResultWrapper.success(getVoById(id));
    }

    /**
     * 根据ID和VoProps获取记录
     * @param id ID
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<Vo> resultById(ID id, VoProps voProps) throws BusinessException {
        return ResultWrapper.success(getVoById(id, voProps));
    }

    /**
     * 根据QueryWrapper获取记录
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<Vo> resultOne(Wrapper<T> queryWrapper) throws BusinessException {
        return ResultWrapper.success(getVo(queryWrapper));
    }

    /**
     * 根据QueryWrapper和VoProps获取记录
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<Vo> resultOne(Wrapper<T> queryWrapper, VoProps voProps) throws BusinessException {
        return ResultWrapper.success(getVo(queryWrapper, voProps));
    }

    /**
     * 获取所有记录列表
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultListAll() {
        return ResultWrapper.success(listAllVo());
    }

    /**
     * 根据VoProps获取所有记录列表
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultListAll(VoProps voProps) {
        return ResultWrapper.success(listAllVo(voProps));
    }

    /**
     * 根据ID集合获取记录列表
     * @param ids ID集合
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultListByIds(Collection<ID> ids) {
        return ResultWrapper.success(listVoByIds(ids));
    }

    /**
     * 根据ID集合和VoProps获取记录列表
     * @param ids ID集合
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultListByIds(Collection<ID> ids, VoProps voProps) {
        return ResultWrapper.success(listVoByIds(ids, voProps));
    }

    /**
     * 根据QueryWrapper获取记录列表
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultList(Wrapper<T> queryWrapper) {
        return ResultWrapper.success(listVo(queryWrapper));
    }

    /**
     * 根据QueryWrapper和VoProps获取记录列表
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<List<Vo>> resultList(Wrapper<T> queryWrapper, VoProps voProps) {
        return ResultWrapper.success(listVo(queryWrapper, voProps));
    }

    /**
     * 根据IPage分页查询对象获取分页数据
     * @param page 分页查询参数
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page) {
        return ResultWrapper.success(pageVo(page));
    }

    /**
     * 根据IPage分页查询对象和AggregateProps获取包含聚合信息的分页数据
     * @param page 分页查询参数
     * @param aggregateProps aggregateProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, AggregateProps aggregateProps) {
        return ResultWrapper.success(pageAggregate(pageVo(page), new QueryWrapper<>(), aggregateProps));
    }

    /**
     * 根据IPage分页查询对象和VoProps获取分页数据
     * @param page 分页查询参数
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, VoProps voProps) {
        return ResultWrapper.success(pageVo(page, voProps));
    }

    /**
     * 根据IPage分页查询对象、AggregateProps和VoProps获取包含聚合信息的分页数据
     * @param page 分页查询参数
     * @param aggregateProps aggregateProps对象
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, AggregateProps aggregateProps, VoProps voProps) {
        return ResultWrapper.success(pageAggregate(pageVo(page, voProps), new QueryWrapper<>(), aggregateProps));
    }

    /**
     * 根据IPage分页查询对象和QueryWrapper获取分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, Wrapper<T> queryWrapper) {
        return ResultWrapper.success(pageVo(page, queryWrapper));
    }

    /**
     * 根据IPage分页查询对象、QueryWrapper和AggregateProps获取包含聚合信息的分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param aggregateProps aggregateProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, Wrapper<T> queryWrapper, AggregateProps aggregateProps) {
        return ResultWrapper.success(pageAggregate(pageVo(page, queryWrapper), queryWrapper, aggregateProps));
    }

    /**
     * 根据IPage分页查询对象、QueryWrapper和VoProps获取分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, Wrapper<T> queryWrapper, VoProps voProps) {
        return ResultWrapper.success(pageVo(page, queryWrapper, voProps));
    }

    /**
     * 根据IPage分页查询对象、QueryWrapper、AggregateProps和VoProps获取包含聚合信息的分页数据
     * @param page 分页查询参数
     * @param queryWrapper queryWrapper查询对象
     * @param aggregateProps aggregateProps对象
     * @param voProps VoProps对象
     * @param <Vo> Vo类型
     */
    public <Vo extends BaseVo> Result<IPage<Vo>> resultPage(IPage<T> page, Wrapper<T> queryWrapper, AggregateProps aggregateProps, VoProps voProps) {
        return ResultWrapper.success(pageAggregate(pageVo(page, queryWrapper, voProps), queryWrapper, aggregateProps));
    }

    /**
     * 保存Dto
     * @param dto dto对象
     * @param <Dto> Dto类型
     */
    public <Dto extends BaseDto> Result resultSaveDto(Dto dto) throws BusinessException {
        if (saveDto(dto)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.SAVE_FAIL);
    }

    /**
     * 保存Dto并返回保存的Vo对象
     * @param dto dto对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<Vo> resultSaveDtoAndReturn(Dto dto) throws BusinessException {
        return resultSaveDtoAndReturn(dto, VoProps.defaultProps());
    }

    /**
     * 保存Dto并根据VoProps返回保存的Vo对象
     * @param dto dto对象
     * @param voProps VoProps对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    @SuppressWarnings("unchecked")
    public <Dto extends BaseDto, Vo extends BaseVo> Result<Vo> resultSaveDtoAndReturn(Dto dto, VoProps voProps) throws BusinessException {
        if (!dto.isValid()) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, dto);
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dto: %s", dto));
        }
        checkUnique(dto);
        T model = modelFromDto(dto);
        ServiceContext.removeModelBuilder();
        if (saveOrUpdate(model)) {
            Vo vo = fromModel(model, voProps);
            clearVoConfig();
            return ResultWrapper.success(vo);
        }
        return ResultWrapper.fail(ResultCode.SAVE_FAIL);
    }

    /**
     * 批量保存Dto
     * @param dtos dto集合
     * @param <Dto> Dto类型
     */
    public <Dto extends BaseDto> Result resultSaveBatchDto(Collection<Dto> dtos) throws BusinessException {
        if (saveBatchDto(dtos)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.SAVE_FAIL);
    }

    /**
     * 设置分批次保存记录数并批量保存Dto
     * @param dtos dto集合
     * @param batchSize 分批次记录数
     * @param <Dto> Dto类型
     */
    public <Dto extends BaseDto> Result resultSaveBatchDto(Collection<Dto> dtos, int batchSize) throws BusinessException {
        if (saveBatchDto(dtos, batchSize)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.SAVE_FAIL);
    }

    /**
     * 批量保存Dto并返回保存的Vo列表
     * @param dtos dto集合
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultSaveBatchDtoAndReturn(Collection<Dto> dtos) throws BusinessException {
        return resultSaveBatchDtoAndReturn(dtos, VoProps.defaultProps());
    }

    /**
     * 批量保存Dto并根据VoProps返回保存的Vo列表
     * @param dtos dto集合
     * @param voProps VoProps对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultSaveBatchDtoAndReturn(Collection<Dto> dtos, VoProps voProps) throws BusinessException {
        return resultSaveBatchDtoAndReturn(dtos, 1000, voProps);
    }

    /**
     * 设置分批次保存记录数并批量保存Dto，同时返回保存的Vo列表
     * @param dtos dto集合
     * @param batchSize 分批次记录数
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultSaveBatchDtoAndReturn(Collection<Dto> dtos, int batchSize) throws BusinessException {
        return resultSaveBatchDtoAndReturn(dtos, batchSize, VoProps.defaultProps());
    }

    /**
     * 设置分批次保存记录数并批量保存Dto，根据VoProps返回保存的Vo列表
     * @param dtos dto集合
     * @param batchSize 分批次记录数
     * @param voProps VoProps对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    @SuppressWarnings("unchecked")
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultSaveBatchDtoAndReturn(Collection<Dto> dtos, int batchSize, VoProps voProps) throws BusinessException {
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
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, Arrays.toString(invalidDtos.toArray()));
        }
        ServiceContext.removeModelBuilder();
        if (saveOrUpdateBatch(models, batchSize)) {
            List<Vo> voList = models.stream().map(model -> {
                Vo vo = null;
                try {
                    vo = fromModel(model, voProps);
                } catch (BusinessException e) {
                    logger.error(LogConstants.EXCEPTION_INFO, e);
                }
                return vo;
            }).collect(Collectors.toList());
            clearVoConfig();
            return ResultWrapper.success(voList);
        }
        return ResultWrapper.fail(ResultCode.SAVE_FAIL);
    }

    /**
     * 根据QueryWrapper更新Dto
     * @param updateDto 更新dto对象
     * @param queryWrapper queryWrapper查询对象
     * @param <Dto> Dto类型
     */
    public <Dto extends BaseDto> Result resultUpdateDto(Dto updateDto, @NonNull QueryWrapper<T> queryWrapper) throws BusinessException {
        if (updateDto(updateDto, queryWrapper)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.UPDATE_FAIL);
    }

    /**
     * 根据QueryWrapper更新Dto并返回更新后的Vo
     * @param updateDto 更新dto对象
     * @param queryWrapper queryWrapper查询对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<Vo> resultUpdateDtoAndReturn(Dto updateDto, QueryWrapper<T> queryWrapper) throws BusinessException {
        return resultUpdateDtoAndReturn(updateDto, queryWrapper, VoProps.defaultProps());
    }

    /**
     * 根据QueryWrapper更新Dto并根据VoProps返回更新后的Vo
     * @param updateDto 更新dto对象
     * @param queryWrapper queryWrapper查询对象
     * @param voProps VoProps对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    @SuppressWarnings("unchecked")
    public <Dto extends BaseDto, Vo extends BaseVo> Result<Vo> resultUpdateDtoAndReturn(Dto updateDto, @NonNull QueryWrapper<T> queryWrapper, VoProps voProps) throws BusinessException {
        if (!updateDto.isValid()) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, updateDto);
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dto: %s", updateDto));
        }
        //数据权限过滤
        queryWrapper.eq("id", updateDto.fetchId());
        if (getOne(queryWrapper) == null) {
            return ResultWrapper.fail(ResultCode.UPDATE_FAIL);
        }
        //更新数据
        checkUnique(updateDto);
        T model = modelFromDto(updateDto);
        ServiceContext.removeModelBuilder();
        if (updateById(model)) {
            Vo vo = fromModel(model, voProps);
            clearVoConfig();
            return ResultWrapper.success(vo);
        }
        return ResultWrapper.fail(ResultCode.UPDATE_FAIL);
    }

    /**
     * 批量更新Dto
     * @param dtos 更新dto集合
     * @param <Dto> Dto类型
     */
    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public <Dto extends BaseDto> Result resultUpdateBatchDto(Collection<Dto> dtos, @NonNull QueryWrapper<T> queryWrapper) throws BusinessException {
        if (!BaseDto.isValid(dtos)) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, Arrays.toString(dtos.toArray()));
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dtos: %s", Arrays.toString(dtos.toArray())));
        }
        //数据权限过滤
        List<ID> ids = listObjs(queryWrapper.select(), id -> (ID) id);
        if (CollectionUtils.isNotEmpty(ids)) {
            dtos.removeIf(dto -> !ids.contains(dto.fetchId()));
            //更新数据
            if (saveBatchDto(dtos)) {
                return ResultWrapper.success();
            }
        }
        return ResultWrapper.fail(ResultCode.UPDATE_FAIL);
    }

    /**
     * 批量保存Dto并返回保存的Vo列表
     * @param dtos dto集合
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultUpdateBatchDtoAndReturn(Collection<Dto> dtos, QueryWrapper<T> queryWrapper) throws BusinessException {
        return resultUpdateBatchDtoAndReturn(dtos, queryWrapper, VoProps.defaultProps());
    }

    /**
     * 根据QueryWrapper批量更新Dto并返回保存的Vo列表
     * @param dtos 更新dto集合
     * @param queryWrapper queryWrapper查询对象
     * @param voProps voProps对象
     * @param <Dto> Dto类型
     * @param <Vo> Vo类型
     */
    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public <Dto extends BaseDto, Vo extends BaseVo> Result<List<Vo>> resultUpdateBatchDtoAndReturn(Collection<Dto> dtos, @NonNull QueryWrapper<T> queryWrapper, VoProps voProps) throws BusinessException {
        if (!BaseDto.isValid(dtos)) {
            logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, Arrays.toString(dtos.toArray()));
            throw BusinessException.from(ResultCode.PARAMS_ERROR, String.format("dtos: %s", Arrays.toString(dtos.toArray())));
        }
        //数据权限过滤
        List<ID> ids = getObj(queryWrapper.select(), result -> (List<ID>) result);
        if (CollectionUtils.isNotEmpty(ids)) {
            dtos.removeIf(dto -> !ids.contains(dto.fetchId()));
            //更新数据
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
                logger.warn(LogConstants.VALIDATE_FAIL_WITH_PARAM, Arrays.toString(invalidDtos.toArray()));
            }
            ServiceContext.removeModelBuilder();
            if (updateBatchById(models)) {
                List<Vo> voList = models.stream().map(model -> {
                    Vo vo = null;
                    try {
                        vo = fromModel(model, voProps);
                    } catch (BusinessException e) {
                        logger.error(LogConstants.EXCEPTION_INFO, e);
                    }
                    return vo;
                }).collect(Collectors.toList());
                clearVoConfig();
                return ResultWrapper.success(voList);
            }

        }
        return ResultWrapper.fail(ResultCode.UPDATE_FAIL);
    }

    /**
     * 根据ID删除记录
     * @param id ID
     */
    public Result resultDeleteById(ID id) {
        if (removeById(id)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.DELETE_FAIL);
    }

    /**
     * 根据ID集合批量删除记录
     * @param ids ID集合
     */
    public Result resultDeleteBatchByIds(Collection<ID> ids) {
        if (removeByIds(ids)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.DELETE_FAIL);
    }

    /**
     * 根据QueryWrapper删除记录
     * @param queryWrapper queryWrapper查询对象
     */
    public Result resultDelete(@NonNull QueryWrapper<T> queryWrapper) {
        if (remove(queryWrapper)) {
            return ResultWrapper.success();
        }
        return ResultWrapper.fail(ResultCode.DELETE_FAIL);
    }
}
