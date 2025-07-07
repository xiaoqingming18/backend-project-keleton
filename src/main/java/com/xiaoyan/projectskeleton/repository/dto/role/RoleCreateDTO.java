package com.xiaoyan.projectskeleton.repository.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色创建DTO
 */
@Data
public class RoleCreateDTO {
    
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String name;
    
    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Pattern(regexp = "^[A-Z_]{1,50}$", message = "角色编码只能包含大写字母和下划线")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String code;
    
    /**
     * 角色描述
     */
    @Size(max = 255, message = "角色描述长度不能超过255个字符")
    private String description;
    
    /**
     * 排序
     */
    private Integer sort = 0;
    
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
} 