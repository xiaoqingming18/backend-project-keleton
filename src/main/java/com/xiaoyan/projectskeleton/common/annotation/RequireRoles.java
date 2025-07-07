package com.xiaoyan.projectskeleton.common.annotation;

import com.xiaoyan.projectskeleton.common.enums.RoleEnum;
import java.lang.annotation.*;

/**
 * 需要角色注解
 * 标记在Controller方法上，表示该接口需要特定角色才能访问
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRoles {
    /**
     * 允许访问的角色枚举列表
     * 为空时表示不限制角色
     */
    RoleEnum[] value() default {};
    
    /**
     * 逻辑类型
     * 默认为OR，表示满足任一角色即可访问
     * 设置为AND时，表示需要同时满足所有角色才能访问
     */
    Logical logical() default Logical.OR;
    
    /**
     * 逻辑类型枚举
     */
    enum Logical {
        /**
         * 满足任一角色即可
         */
        OR,
        /**
         * 必须满足所有角色
         */
        AND
    }
} 