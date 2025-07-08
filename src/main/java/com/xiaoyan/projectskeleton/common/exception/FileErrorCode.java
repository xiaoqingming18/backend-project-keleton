package com.xiaoyan.projectskeleton.common.exception;

import lombok.Getter;

/**
 * 文件操作错误码
 */
@Getter
public enum FileErrorCode implements ErrorCode {

    /**
     * 文件不能为空
     */
    FILE_EMPTY(50001, "文件不能为空"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_SUPPORTED(50002, "文件类型不支持"),

    /**
     * 文件大小超过限制
     */
    FILE_SIZE_EXCEEDED(50003, "文件大小超过限制"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED(50004, "文件上传失败"),

    /**
     * 文件不存在
     */
    FILE_NOT_EXIST(50005, "文件不存在"),

    /**
     * 文件删除失败
     */
    FILE_DELETE_FAILED(50006, "文件删除失败");

    private final Integer code;
    private final String message;

    FileErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 