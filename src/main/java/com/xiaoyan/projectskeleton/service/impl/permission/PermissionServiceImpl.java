package com.xiaoyan.projectskeleton.service.impl.permission;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.enums.PermissionTypeEnum;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.PermissionErrorCode;
import com.xiaoyan.projectskeleton.common.exception.RoleErrorCode;
import com.xiaoyan.projectskeleton.mapper.permission.PermissionMapper;
import com.xiaoyan.projectskeleton.mapper.permission.RolePermissionMapper;
import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionCreateDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.PermissionDTO;
import com.xiaoyan.projectskeleton.repository.dto.permission.RolePermissionAssignDTO;
import com.xiaoyan.projectskeleton.repository.entity.permission.Permission;
import com.xiaoyan.projectskeleton.repository.entity.permission.RolePermission;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import com.xiaoyan.projectskeleton.service.permission.PermissionService;
import com.xiaoyan.projectskeleton.service.role.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private RoleService roleService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermission(PermissionCreateDTO createDTO) {
        // 校验权限类型是否合法
        PermissionTypeEnum typeEnum = PermissionTypeEnum.getById(createDTO.getType());
        if (typeEnum == null) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_TYPE_INVALID);
        }
        
        // 校验权限编码是否已存在
        LambdaQueryWrapper<Permission> codeQuery = new LambdaQueryWrapper<>();
        codeQuery.eq(Permission::getCode, createDTO.getCode());
        if (this.count(codeQuery) > 0) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_CODE_ALREADY_EXISTS);
        }
        
        // 校验权限名称是否已存在
        LambdaQueryWrapper<Permission> nameQuery = new LambdaQueryWrapper<>();
        nameQuery.eq(Permission::getName, createDTO.getName());
        if (this.count(nameQuery) > 0) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_NAME_ALREADY_EXISTS);
        }
        
        // 如果父权限ID不为0，校验父权限是否存在
        if (createDTO.getParentId() != null && createDTO.getParentId() > 0) {
            Permission parentPermission = this.getById(createDTO.getParentId());
            if (parentPermission == null) {
                throw new BusinessException(PermissionErrorCode.PARENT_PERMISSION_NOT_EXISTS);
            }
        }
        
        // 创建权限
        Permission permission = new Permission();
        BeanUtils.copyProperties(createDTO, permission);
        
        // 设置默认值
        if (permission.getParentId() == null) {
            permission.setParentId(0L);
        }
        if (permission.getSort() == null) {
            permission.setSort(0);
        }
        
        // 保存权限
        this.save(permission);
        
        return permission.getId();
    }
    
    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = this.getById(id);
        if (permission == null) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_NOT_EXISTS);
        }
        
        return convertToDTO(permission);
    }
    
    @Override
    public List<PermissionDTO> getPermissionTree() {
        // 获取所有权限
        List<Permission> permissions = this.list();
        
        // 转换为DTO
        List<PermissionDTO> permissionDTOs = permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 构建树形结构
        return buildTree(permissionDTOs);
    }
    
    @Override
    public List<PermissionDTO> getAllPermissions() {
        // 获取所有权限
        List<Permission> permissions = this.list();
        
        // 转换为DTO
        return permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PermissionDTO> getPermissionsByRoleId(Long roleId) {
        // 检查角色是否存在
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new BusinessException(RoleErrorCode.ROLE_NOT_EXISTS);
        }
        
        // 查询角色权限关联
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);
        
        // 如果没有权限，返回空列表
        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
        
        // 查询权限
        List<Permission> permissions = this.listByIds(permissionIds);
        
        // 转换为DTO
        return permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissionsToRole(RolePermissionAssignDTO assignDTO) {
        // 检查角色是否存在
        Role role = roleService.getById(assignDTO.getRoleId());
        if (role == null) {
            throw new BusinessException(RoleErrorCode.ROLE_NOT_EXISTS);
        }
        
        // 检查权限是否都存在
        List<Permission> permissions = this.listByIds(assignDTO.getPermissionIds());
        if (permissions.size() != assignDTO.getPermissionIds().size()) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_NOT_EXISTS);
        }
        
        // 先删除角色原有的权限
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, assignDTO.getRoleId());
        rolePermissionMapper.delete(queryWrapper);
        
        // 如果权限ID列表为空，则表示清空角色权限
        if (assignDTO.getPermissionIds().isEmpty()) {
            return true;
        }
        
        // 批量插入新的角色权限关联
        List<RolePermission> rolePermissions = assignDTO.getPermissionIds().stream()
                .map(permissionId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(assignDTO.getRoleId());
                    rolePermission.setPermissionId(permissionId);
                    return rolePermission;
                })
                .collect(Collectors.toList());
        
        // 批量保存
        for (RolePermission rolePermission : rolePermissions) {
            rolePermissionMapper.insert(rolePermission);
        }
        
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long id) {
        // 检查权限是否存在
        Permission permission = this.getById(id);
        if (permission == null) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_NOT_EXISTS);
        }
        
        // 检查是否有子权限
        LambdaQueryWrapper<Permission> childQuery = new LambdaQueryWrapper<>();
        childQuery.eq(Permission::getParentId, id);
        long childCount = this.count(childQuery);
        if (childCount > 0) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_HAS_CHILDREN);
        }
        
        // 删除角色权限关联
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getPermissionId, id);
        rolePermissionMapper.delete(queryWrapper);
        
        // 逻辑删除权限
        return this.removeById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removePermissionFromRole(Long roleId, Long permissionId) {
        // 检查角色是否存在
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new BusinessException(RoleErrorCode.ROLE_NOT_EXISTS);
        }
        
        // 检查权限是否存在
        Permission permission = this.getById(permissionId);
        if (permission == null) {
            throw new BusinessException(PermissionErrorCode.PERMISSION_NOT_EXISTS);
        }
        
        // 删除角色权限关联
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId)
                .eq(RolePermission::getPermissionId, permissionId);
        int result = rolePermissionMapper.delete(queryWrapper);
        
        return result > 0;
    }
    
    /**
     * 将权限实体转换为DTO
     *
     * @param permission 权限实体
     * @return 权限DTO
     */
    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        BeanUtils.copyProperties(permission, dto);
        
        // 设置权限类型名称
        PermissionTypeEnum typeEnum = PermissionTypeEnum.getById(permission.getType());
        if (typeEnum != null) {
            dto.setTypeName(typeEnum.getName());
        }
        
        // 如果有父权限，设置父权限名称
        if (permission.getParentId() != null && permission.getParentId() > 0) {
            Permission parentPermission = this.getById(permission.getParentId());
            if (parentPermission != null) {
                dto.setParentName(parentPermission.getName());
            }
        }
        
        return dto;
    }
    
    /**
     * 构建权限树
     *
     * @param permissionDTOs 权限DTO列表
     * @return 权限树
     */
    private List<PermissionDTO> buildTree(List<PermissionDTO> permissionDTOs) {
        // 创建根节点列表
        List<PermissionDTO> rootPermissions = new ArrayList<>();
        
        // 创建ID到权限DTO的映射
        Map<Long, PermissionDTO> permissionMap = new HashMap<>();
        for (PermissionDTO permissionDTO : permissionDTOs) {
            permissionMap.put(permissionDTO.getId(), permissionDTO);
        }
        
        // 构建树形结构
        for (PermissionDTO permissionDTO : permissionDTOs) {
            // 如果是根节点
            if (permissionDTO.getParentId() == null || permissionDTO.getParentId() == 0) {
                rootPermissions.add(permissionDTO);
            } else {
                // 如果不是根节点，添加到父节点的子节点列表中
                PermissionDTO parentPermission = permissionMap.get(permissionDTO.getParentId());
                if (parentPermission != null) {
                    if (parentPermission.getChildren() == null) {
                        parentPermission.setChildren(new ArrayList<>());
                    }
                    parentPermission.getChildren().add(permissionDTO);
                }
            }
        }
        
        return rootPermissions;
    }
} 