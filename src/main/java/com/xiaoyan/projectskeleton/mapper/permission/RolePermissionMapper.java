package com.xiaoyan.projectskeleton.mapper.permission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.projectskeleton.repository.entity.permission.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联Mapper接口
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
} 