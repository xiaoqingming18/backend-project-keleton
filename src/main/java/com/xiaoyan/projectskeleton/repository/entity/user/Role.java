package com.xiaoyan.projectskeleton.repository.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyan.projectskeleton.repository.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("role")
public class Role extends BaseEntity {
    
    /**
     * 角色名称
     */
    private String name;
    
    /**
     * 角色编码
     */
    private String code;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 排序
     */
    private Integer sort;
} 