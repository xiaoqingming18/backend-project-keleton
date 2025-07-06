package com.xiaoyan.projectskeleton.service;

import com.xiaoyan.projectskeleton.repository.dto.user.RoleDTO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<RoleDTO> listAllRoles();
} 