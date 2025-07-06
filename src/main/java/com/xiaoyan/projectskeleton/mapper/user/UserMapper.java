package com.xiaoyan.projectskeleton.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 