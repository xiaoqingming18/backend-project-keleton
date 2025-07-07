package com.xiaoyan.projectskeleton.repository.entity.role;

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
    
    /**
     * 获取角色名称
     * @return 角色名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取角色编码
     * @return 角色编码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取角色描述
     * @return 角色描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 获取排序
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }
    
    /**
     * 设置角色名称
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 设置角色编码
     * @param code 角色编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * 设置角色描述
     * @param description 角色描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * 设置排序
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
} 