package com.xiaoyan.projectskeleton.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    /**
     * 根据角色编码查询角色
     * @param code 角色编码
     * @return 角色对象
     */
    @Select("SELECT * FROM role WHERE code = #{code} AND deleted = 0")
    Role findByCode(@Param("code") String code);
} 