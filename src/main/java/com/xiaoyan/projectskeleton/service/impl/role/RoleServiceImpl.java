package com.xiaoyan.projectskeleton.service.impl.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.context.UserContext;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.ExceptionUtils;
import com.xiaoyan.projectskeleton.common.exception.RoleErrorCode;
import com.xiaoyan.projectskeleton.mapper.role.RoleMapper;
import com.xiaoyan.projectskeleton.repository.dto.role.RoleDTO;
import com.xiaoyan.projectskeleton.repository.dto.role.RoleCreateDTO;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import com.xiaoyan.projectskeleton.service.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @Override
    public List<RoleDTO> listAllRoles() {
        // 查询所有未删除的角色，按排序字段升序排列
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getSort);
        
        List<Role> roleList = this.list(queryWrapper);
        
        // 转换为DTO
        return roleList.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取角色
     *
     * @param id 角色ID
     * @return 角色对象
     */
    @Override
    public Role getById(Long id) {
        return super.getById(id);
    }
    
    /**
     * 创建角色
     *
     * @param createDTO 角色创建DTO
     * @return 创建后的角色DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO createRole(RoleCreateDTO createDTO) {
        // 1. 检查角色编码是否已存在
        ExceptionUtils.assertFalse(checkCodeExists(createDTO.getCode()), 
                RoleErrorCode.ROLE_CODE_ALREADY_EXISTS);
        
        // 2. 创建角色实体
        Role role = new Role();
        role.setName(createDTO.getName());
        role.setCode(createDTO.getCode());
        role.setDescription(createDTO.getDescription());
        role.setSort(createDTO.getSort() != null ? createDTO.getSort() : 0);
        
        // 3. 保存角色
        this.save(role);
        
        // 4. 记录日志
        UserContext userContext = UserContext.getCurrentUser();
        String operator = userContext != null ? userContext.getUsername() : "system";
        log.info("角色 {} 由 {} 创建成功", role.getName(), operator);
        
        // 5. 转换为DTO并返回
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }
    
    /**
     * 检查角色编码是否已存在
     *
     * @param code 角色编码
     * @return 是否存在
     */
    @Override
    public boolean checkCodeExists(String code) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getCode, code);
        return this.count(queryWrapper) > 0;
    }
} 