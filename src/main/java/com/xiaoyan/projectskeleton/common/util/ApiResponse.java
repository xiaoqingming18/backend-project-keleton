package com.xiaoyan.projectskeleton.common.util;

import java.io.Serializable;

/**
 * 通用API响应结果封装
 */
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 私有构造函数
     */
    private ApiResponse() {
    }

    /**
     * 私有构造函数
     *
     * @param code    状态码
     * @param success 是否成功
     * @param message 消息
     * @param data    数据
     */
    private ApiResponse(Integer code, Boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     * @param <T>     数据泛型
     * @return 通用返回结果
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, true, message, data);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 提示信息
     * @param <T>     数据泛型
     * @return 通用返回结果
     */
    public static <T> ApiResponse<T> failed(Integer code, String message) {
        return new ApiResponse<>(code, false, message, null);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 