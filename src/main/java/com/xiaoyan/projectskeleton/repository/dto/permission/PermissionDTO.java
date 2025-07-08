package com.xiaoyan.projectskeleton.repository.dto.permission;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限数据传输对象
 */
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    private Long id;

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
     * 权限类型名称
     */
    private String typeName;

    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 父权限名称
     */
    private String parentName;

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
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 子权限列表
     */
    private List<PermissionDTO> children;
    
    /**
     * 获取权限ID
     * @return 权限ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置权限ID
     * @param id 权限ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
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
     * 获取权限类型名称
     * @return 权限类型名称
     */
    public String getTypeName() {
        return typeName;
    }
    
    /**
     * 设置权限类型名称
     * @param typeName 权限类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * 获取父权限名称
     * @return 父权限名称
     */
    public String getParentName() {
        return parentName;
    }
    
    /**
     * 设置父权限名称
     * @param parentName 父权限名称
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
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
    
    /**
     * 获取创建时间
     * @return 创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取子权限列表
     * @return 子权限列表
     */
    public List<PermissionDTO> getChildren() {
        return children;
    }
    
    /**
     * 设置子权限列表
     * @param children 子权限列表
     */
    public void setChildren(List<PermissionDTO> children) {
        this.children = children;
    }
} 