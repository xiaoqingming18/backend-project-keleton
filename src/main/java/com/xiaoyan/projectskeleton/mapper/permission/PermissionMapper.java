package com.xiaoyan.projectskeleton.mapper.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.projectskeleton.repository.entity.permission.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限Mapper接口
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
} 