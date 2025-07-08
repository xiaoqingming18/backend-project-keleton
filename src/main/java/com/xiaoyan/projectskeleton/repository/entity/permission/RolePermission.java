package com.xiaoyan.projectskeleton.repository.entity.permission;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyan.projectskeleton.repository.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色权限关联实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("role_permission")
public class RolePermission extends BaseEntity {
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 权限ID
     */
    private Long permissionId;
    
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
     * 获取权限ID
     * @return 权限ID
     */
    public Long getPermissionId() {
        return permissionId;
    }
    
    /**
     * 设置权限ID
     * @param permissionId 权限ID
     */
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
} 