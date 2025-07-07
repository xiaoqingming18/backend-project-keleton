package com.xiaoyan.projectskeleton.common.exception;

import lombok.Getter;

/**
 * 角色模块错误码枚举
 * 角色模块错误码以 200 开头
 */
@Getter
public enum RoleErrorCode implements ErrorCode {

    /**
     * 角色编码已存在
     */
    ROLE_CODE_ALREADY_EXISTS(20001, "角色编码已存在"),

    /**
     * 角色名称已存在
     */
    ROLE_NAME_ALREADY_EXISTS(20002, "角色名称已存在"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXISTS(20003, "角色不存在"),

    /**
     * 系统内置角色不允许修改
     */
    SYSTEM_ROLE_NOT_ALLOWED_MODIFY(20004, "系统内置角色不允许修改"),

    /**
     * 系统内置角色不允许删除
     */
    SYSTEM_ROLE_NOT_ALLOWED_DELETE(20005, "系统内置角色不允许删除");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    RoleErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 