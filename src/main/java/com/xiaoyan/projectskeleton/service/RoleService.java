package com.xiaoyan.projectskeleton.service;

import com.xiaoyan.projectskeleton.repository.dto.user.RoleDTO;
import com.xiaoyan.projectskeleton.repository.entity.user.Role;

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
    
    /**
     * 根据ID获取角色
     * 
     * @param id 角色ID
     * @return 角色对象
     */
    Role getById(Long id);
} 