package com.xiaoyan.projectskeleton.controller.permission;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.annotation.RequireRoles;
import com.xiaoyan.projectskeleton.common.enums.RoleEnum;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionCreateDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.RolePermissionAssignDTO;
import com.xiaoyan.projectskeleton.service.permission.PermissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    /**
     * 创建权限
     *
     * @param createDTO 权限创建DTO
     * @return 创建的权限ID
     */
    @PostMapping("/create")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<Long> createPermission(@RequestBody @Valid PermissionCreateDTO createDTO) {
        log.info("创建权限: {}", createDTO.getName());
        Long permissionId = permissionService.createPermission(createDTO);
        return ApiResponse.success(permissionId, "创建权限成功");
    }
    
    /**
     * 获取权限树
     *
     * @return 权限树
     */
    @GetMapping("/tree")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<List<PermissionDTO>> getPermissionTree() {
        log.info("获取权限树");
        List<PermissionDTO> permissionTree = permissionService.getPermissionTree();
        return ApiResponse.success(permissionTree, "获取权限树成功");
    }
    
    /**
     * 获取所有权限（平铺结构）
     *
     * @return 权限列表
     */
    @GetMapping("/list")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<List<PermissionDTO>> getAllPermissions() {
        log.info("获取所有权限");
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return ApiResponse.success(permissions, "获取权限列表成功");
    }
    
    /**
     * 根据ID获取权限
     *
     * @param id 权限ID
     * @return 权限DTO
     */
    @GetMapping("/{id}")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<PermissionDTO> getPermissionById(@PathVariable Long id) {
        log.info("根据ID获取权限: {}", id);
        PermissionDTO permission = permissionService.getPermissionById(id);
        return ApiResponse.success(permission, "获取权限成功");
    }
    
    /**
     * 获取角色的权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/role/{roleId}")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<List<PermissionDTO>> getPermissionsByRoleId(@PathVariable Long roleId) {
        log.info("获取角色的权限列表: {}", roleId);
        List<PermissionDTO> permissions = permissionService.getPermissionsByRoleId(roleId);
        return ApiResponse.success(permissions, "获取角色权限列表成功");
    }
    
    /**
     * 为角色分配权限
     *
     * @param assignDTO 角色权限分配DTO
     * @return 是否分配成功
     */
    @PostMapping("/assign")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<Boolean> assignPermissionsToRole(@RequestBody @Valid RolePermissionAssignDTO assignDTO) {
        log.info("为角色分配权限: {}", assignDTO.getRoleId());
        boolean result = permissionService.assignPermissionsToRole(assignDTO);
        return ApiResponse.success(result, "分配权限成功");
    }
    
    /**
     * 删除权限（逻辑删除）
     *
     * @param id 权限ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<Boolean> deletePermission(@PathVariable Long id) {
        log.info("删除权限: {}", id);
        boolean result = permissionService.deletePermission(id);
        return ApiResponse.success(result, "删除权限成功");
    }
    
    /**
     * 取消角色的指定权限
     *
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 是否取消成功
     */
    @DeleteMapping("/role/{roleId}/permission/{permissionId}")
    @RequireLogin
    @RequireRoles({RoleEnum.ADMIN})
    public ApiResponse<Boolean> removePermissionFromRole(@PathVariable Long roleId, @PathVariable Long permissionId) {
        log.info("取消角色的指定权限: 角色ID={}, 权限ID={}", roleId, permissionId);
        boolean result = permissionService.removePermissionFromRole(roleId, permissionId);
        return ApiResponse.success(result, "取消权限成功");
    }
} 