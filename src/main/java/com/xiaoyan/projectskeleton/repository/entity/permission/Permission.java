package com.xiaoyan.projectskeleton.repository.entity.permission;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyan.projectskeleton.repository.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("permission")
public class Permission extends BaseEntity {
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限编码
     */
    private String code;
    
    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer type;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 权限路径
     */
    private String path;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 获取权限名称
     * @return 权限名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置权限名称
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取权限编码
     * @return 权限编码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 设置权限编码
     * @param code 权限编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * 获取权限类型
     * @return 权限类型
     */
    public Integer getType() {
        return type;
    }
    
    /**
     * 设置权限类型
     * @param type 权限类型
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
    /**
     * 获取父权限ID
     * @return 父权限ID
     */
    public Long getParentId() {
        return parentId;
    }
    
    /**
     * 设置父权限ID
     * @param parentId 父权限ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    /**
     * 获取权限路径
     * @return 权限路径
     */
    public String getPath() {
        return path;
    }
    
    /**
     * 设置权限路径
     * @param path 权限路径
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * 获取图标
     * @return 图标
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * 设置图标
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    /**
     * 获取排序
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }
    
    /**
     * 设置排序
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
} 