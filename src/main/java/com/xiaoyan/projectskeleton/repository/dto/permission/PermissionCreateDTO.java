package com.xiaoyan.projectskeleton.repository.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 权限创建DTO
 */
@Data
public class PermissionCreateDTO {
    
    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    private String name;
    
    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Pattern(regexp = "^[A-Z_]{1,50}$", message = "权限编码只能包含大写字母和下划线")
    @Size(max = 50, message = "权限编码长度不能超过50个字符")
    private String code;
    
    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    @NotNull(message = "权限类型不能为空")
    private Integer type;
    
    /**
     * 父权限ID，如果是顶级权限则为0
     */
    private Long parentId = 0L;
    
    /**
     * 权限路径，如果是菜单，则为前端路由路径；如果是接口，则为后端接口路径
     */
    private String path;
    
    /**
     * 图标，仅对菜单类型有效
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sort = 0;
    
    /**
     * 获取权限名称
     * @return 权限名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取权限编码
     * @return 权限编码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取权限类型
     * @return 权限类型
     */
    public Integer getType() {
        return type;
    }
    
    /**
     * 获取父权限ID
     * @return 父权限ID
     */
    public Long getParentId() {
        return parentId;
    }
    
    /**
     * 获取权限路径
     * @return 权限路径
     */
    public String getPath() {
        return path;
    }
    
    /**
     * 获取图标
     * @return 图标
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * 获取排序
     * @return 排序
     */
    public Integer getSort() {
        return sort;
    }
} 