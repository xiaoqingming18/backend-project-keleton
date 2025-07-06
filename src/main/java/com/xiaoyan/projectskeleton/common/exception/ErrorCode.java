package com.xiaoyan.projectskeleton.common.exception;

/**
 * 错误码接口
 */
public interface ErrorCode {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    Integer getCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();
} 