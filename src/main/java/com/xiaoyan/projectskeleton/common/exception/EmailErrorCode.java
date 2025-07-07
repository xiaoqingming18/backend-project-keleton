package com.xiaoyan.projectskeleton.common.exception;

import lombok.Getter;

/**
 * 邮件模块错误码枚举
 * 邮件模块错误码以 500 开头
 */
@Getter
public enum EmailErrorCode implements ErrorCode {

    /**
     * 邮件发送失败
     */
    EMAIL_SEND_FAILED(50001, "邮件发送失败"),

    /**
     * 邮件参数不完整
     */
    EMAIL_PARAMS_INCOMPLETE(50002, "邮件参数不完整"),

    /**
     * 邮件附件处理失败
     */
    EMAIL_ATTACHMENT_ERROR(50003, "邮件附件处理失败"),

    /**
     * 邮件配置错误
     */
    EMAIL_CONFIG_ERROR(50004, "邮件配置错误");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    EmailErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 