package com.xiaoyan.projectskeleton.common.exception;

/**
 * 异常工具类
 */
public class ExceptionUtils {

    /**
     * 抛出业务异常
     *
     * @param errorCode 错误码
     */
    public static void throwBizException(ErrorCode errorCode) {
        throw new BusinessException(errorCode);
    }

    /**
     * 抛出业务异常
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static void throwBizException(ErrorCode errorCode, String message) {
        throw new BusinessException(errorCode, message);
    }

    /**
     * 抛出业务异常
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static void throwBizException(Integer code, String message) {
        throw new BusinessException(code, message);
    }

    /**
     * 断言为真，否则抛出业务异常
     *
     * @param expression 表达式
     * @param errorCode  错误码
     */
    public static void assertTrue(boolean expression, ErrorCode errorCode) {
        if (!expression) {
            throwBizException(errorCode);
        }
    }

    /**
     * 断言为假，否则抛出业务异常
     *
     * @param expression 表达式
     * @param errorCode  错误码
     */
    public static void assertFalse(boolean expression, ErrorCode errorCode) {
        if (expression) {
            throwBizException(errorCode);
        }
    }

    /**
     * 断言对象不为空，否则抛出业务异常
     *
     * @param object    对象
     * @param errorCode 错误码
     */
    public static void assertNotNull(Object object, ErrorCode errorCode) {
        if (object == null) {
            throwBizException(errorCode);
        }
    }

    /**
     * 断言对象为空，否则抛出业务异常
     *
     * @param object    对象
     * @param errorCode 错误码
     */
    public static void assertNull(Object object, ErrorCode errorCode) {
        if (object != null) {
            throwBizException(errorCode);
        }
    }
} 