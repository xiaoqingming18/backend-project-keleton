package com.xiaoyan.projectskeleton.common.enums;

import lombok.Getter;

/**
 * 角色枚举类
 * 对应数据库中的角色编码
 */
@Getter
public enum RoleEnum {
    
    /**
     * 管理员
     */
    ADMIN("ADMIN", "管理员"),
    
    /**
     * 普通用户
     */
    USER("USER", "普通用户");
    
    /**
     * 角色编码
     */
    private final String code;
    
    /**
     * 角色名称
     */
    private final String name;
    
    /**
     * 构造方法
     *
     * @param code 角色编码
     * @param name 角色名称
     */
    RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    /**
     * 根据编码获取枚举值
     *
     * @param code 角色编码
     * @return 枚举值
     */
    public static RoleEnum getByCode(String code) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum;
            }
        }
        return null;
    }
} 