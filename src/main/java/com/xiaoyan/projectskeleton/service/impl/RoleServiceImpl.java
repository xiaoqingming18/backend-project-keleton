package com.xiaoyan.projectskeleton.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.mapper.user.RoleMapper;
import com.xiaoyan.projectskeleton.repository.dto.user.RoleDTO;
import com.xiaoyan.projectskeleton.repository.entity.user.Role;
import com.xiaoyan.projectskeleton.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
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
} 