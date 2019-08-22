package com.yupaits.sample.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupaits.sample.user.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yupaits
 * @date 2019/8/22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
