package com.xiaoyan.projectskeleton.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举类
 * 对应数据库中的用户状态
 */
@Getter
public enum UserStatusEnum {
    
    /**
     * 未激活
     */
    NOT_ACTIVATED(0, "未激活", "用户已注册但未激活账号"),
    
    /**
     * 正常
     */
    NORMAL(1, "正常", "用户账号正常使用状态"),
    
    /**
     * 封禁
     */
    BANNED(2, "封禁", "用户账号被封禁，无法使用");
    
    /**
     * 状态ID
     */
    private final int id;
    
    /**
     * 状态名称
     */
    private final String name;
    
    /**
     * 状态描述
     */
    private final String description;
    
    /**
     * 构造方法
     *
     * @param id 状态ID
     * @param name 状态名称
     * @param description 状态描述
     */
    UserStatusEnum(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    /**
     * 根据ID获取枚举值
     *
     * @param id 状态ID
     * @return 枚举值
     */
    public static UserStatusEnum getById(int id) {
        for (UserStatusEnum statusEnum : values()) {
            if (statusEnum.getId() == id) {
                return statusEnum;
            }
        }
        return null;
    }
} 