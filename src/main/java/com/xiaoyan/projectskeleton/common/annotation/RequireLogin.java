package com.xiaoyan.projectskeleton.common.annotation;

import java.lang.annotation.*;

/**
 * 需要登录注解
 * 标记在Controller方法上，表示该接口需要用户登录才能访问
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
    /**
     * 是否需要登录
     * 默认为true，表示需要登录
     * 设置为false时，可以不登录访问
     */
    boolean value() default true;
} 