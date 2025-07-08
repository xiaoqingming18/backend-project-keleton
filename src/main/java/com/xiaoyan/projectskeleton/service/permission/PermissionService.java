package com.xiaoyan.projectskeleton.service.permission;

import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionCreateDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.RolePermissionAssignDTO;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 创建权限
     *
     * @param createDTO 权限创建DTO
     * @return 创建的权限ID
     */
    Long createPermission(PermissionCreateDTO createDTO);
    
    /**
     * 根据ID获取权限
     *
     * @param id 权限ID
     * @return 权限DTO
     */
    PermissionDTO getPermissionById(Long id);
    
    /**
     * 获取所有权限（树形结构）
     *
     * @return 权限树
     */
    List<PermissionDTO> getPermissionTree();
    
    /**
     * 获取所有权限（平铺结构）
     *
     * @return 权限列表
     */
    List<PermissionDTO> getAllPermissions();
    
    /**
     * 获取角色的权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionDTO> getPermissionsByRoleId(Long roleId);
    
    /**
     * 为角色分配权限
     *
     * @param assignDTO 角色权限分配DTO
     * @return 是否分配成功
     */
    boolean assignPermissionsToRole(RolePermissionAssignDTO assignDTO);
    
    /**
     * 删除权限（逻辑删除）
     *
     * @param id 权限ID
     * @return 是否删除成功
     */
    boolean deletePermission(Long id);
    
    /**
     * 取消角色的指定权限
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 是否取消成功
     */
    boolean removePermissionFromRole(Long roleId, Long permissionId);
} 