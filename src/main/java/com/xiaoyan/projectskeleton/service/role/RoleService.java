package com.xiaoyan.projectskeleton.service.role;

import com.xiaoyan.projectskeleton.repository.dto.role.RoleDTO;
import com.xiaoyan.projectskeleton.repository.dto.role.RoleCreateDTO;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;

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
    
    /**
     * 创建角色
     * 
     * @param createDTO 角色创建DTO
     * @return 创建后的角色DTO
     */
    RoleDTO createRole(RoleCreateDTO createDTO);
    
    /**
     * 检查角色编码是否已存在
     * 
     * @param code 角色编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);
} 