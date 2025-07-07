package com.xiaoyan.projectskeleton.common.exception;

import lombok.Getter;

/**
 * 用户模块错误码枚举
 * 用户模块错误码以 100 开头
 */
@Getter
public enum UserErrorCode implements ErrorCode {

    /**
     * 用户名已存在
     */
    USERNAME_ALREADY_EXISTS(10001, "用户名已存在"),

    /**
     * 密码不一致
     */
    PASSWORD_NOT_MATCH(10002, "两次输入的密码不一致"),

    /**
     * 邮箱已被注册
     */
    EMAIL_ALREADY_EXISTS(10003, "邮箱已被注册"),

    /**
     * 手机号已被注册
     */
    MOBILE_ALREADY_EXISTS(10004, "手机号已被注册"),

    /**
     * 用户不存在
     */
    USER_NOT_EXISTS(10005, "用户不存在"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(10006, "密码错误"),

    /**
     * 账号已被禁用
     */
    ACCOUNT_DISABLED(10007, "账号已被禁用"),

    /**
     * 账号未激活
     */
    ACCOUNT_NOT_ACTIVATED(10008, "账号未激活"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXISTS(10009, "角色不存在"),
    
    /**
     * 操作不允许
     */
    OPERATION_NOT_ALLOWED(10010, "操作不允许"),
    
    /**
     * 账号已被封禁
     */
    ACCOUNT_ALREADY_BANNED(10011, "账号已被封禁"),
    
    /**
     * 无权限操作
     */
    NO_PERMISSION(10012, "无权限进行此操作"),
    
    /**
     * 验证码不存在或已过期
     */
    VERIFICATION_CODE_EXPIRED(10013, "验证码不存在或已过期"),
    
    /**
     * 验证码错误
     */
    VERIFICATION_CODE_ERROR(10014, "验证码错误"),
    
    /**
     * 邮箱不存在
     */
    EMAIL_NOT_EXISTS(10015, "邮箱不存在"),
    
    /**
     * 验证码发送过于频繁
     */
    VERIFICATION_CODE_SEND_TOO_FREQUENTLY(10016, "验证码发送过于频繁，请稍后再试"),
    
    /**
     * 用户未登录
     */
    USER_NOT_LOGIN(10017, "用户未登录");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    UserErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 