package com.xiaoyan.projectskeleton.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.projectskeleton.repository.entity.user.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资料Mapper接口
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
} 