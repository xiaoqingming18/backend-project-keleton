package com.xiaoyan.projectskeleton.repository.dto.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 角色分配权限DTO
 */
@Data
public class RolePermissionAssignDTO {
    
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    
    /**
     * 权限ID列表
     */
    @NotNull(message = "权限ID列表不能为空")
    private List<Long> permissionIds;
    
    /**
     * 获取角色ID
     * @return 角色ID
     */
    public Long getRoleId() {
        return roleId;
    }
    
    /**
     * 设置角色ID
     * @param roleId 角色ID
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    /**
     * 获取权限ID列表
     * @return 权限ID列表
     */
    public List<Long> getPermissionIds() {
        return permissionIds;
    }
    
    /**
     * 设置权限ID列表
     * @param permissionIds 权限ID列表
     */
    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
} 