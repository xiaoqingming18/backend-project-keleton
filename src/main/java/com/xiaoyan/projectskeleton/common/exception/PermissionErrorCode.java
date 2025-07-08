package com.xiaoyan.projectskeleton.common.exception;

import lombok.Getter;

/**
 * 权限模块错误码枚举
 * 权限模块错误码以 300 开头
 */
@Getter
public enum PermissionErrorCode implements ErrorCode {

    /**
     * 权限编码已存在
     */
    PERMISSION_CODE_ALREADY_EXISTS(30001, "权限编码已存在"),

    /**
     * 权限名称已存在
     */
    PERMISSION_NAME_ALREADY_EXISTS(30002, "权限名称已存在"),

    /**
     * 权限不存在
     */
    PERMISSION_NOT_EXISTS(30003, "权限不存在"),

    /**
     * 父权限不存在
     */
    PARENT_PERMISSION_NOT_EXISTS(30004, "父权限不存在"),

    /**
     * 系统内置权限不允许修改
     */
    SYSTEM_PERMISSION_NOT_ALLOWED_MODIFY(30005, "系统内置权限不允许修改"),

    /**
     * 系统内置权限不允许删除
     */
    SYSTEM_PERMISSION_NOT_ALLOWED_DELETE(30006, "系统内置权限不允许删除"),
    
    /**
     * 权限类型不正确
     */
    PERMISSION_TYPE_INVALID(30007, "权限类型不正确"),
    
    /**
     * 权限有子权限，不能删除
     */
    PERMISSION_HAS_CHILDREN(30008, "该权限有子权限，请先删除子权限");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    PermissionErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 