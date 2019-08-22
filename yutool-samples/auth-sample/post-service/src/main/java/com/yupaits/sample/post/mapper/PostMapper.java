package com.yupaits.sample.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupaits.sample.post.model.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yupaits
 * @date 2019/8/22
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
