package com.xiaoyan.projectskeleton.common.enums;

import lombok.Getter;

/**
 * 权限类型枚举
 */
@Getter
public enum PermissionTypeEnum {
    
    /**
     * 菜单
     */
    MENU(1, "菜单"),
    
    /**
     * 按钮
     */
    BUTTON(2, "按钮"),
    
    /**
     * 接口
     */
    API(3, "接口");
    
    /**
     * 类型ID
     */
    private final Integer id;
    
    /**
     * 类型名称
     */
    private final String name;
    
    /**
     * 构造方法
     *
     * @param id 类型ID
     * @param name 类型名称
     */
    PermissionTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * 获取类型ID
     *
     * @return 类型ID
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * 获取类型名称
     *
     * @return 类型名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 根据ID获取枚举值
     *
     * @param id 类型ID
     * @return 枚举值
     */
    public static PermissionTypeEnum getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (PermissionTypeEnum typeEnum : values()) {
            if (typeEnum.getId().equals(id)) {
                return typeEnum;
            }
        }
        return null;
    }
} 