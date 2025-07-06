package com.xiaoyan.projectskeleton.common.context;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户上下文
 * 用于存储当前登录用户信息
 */
@Data
@Accessors(chain = true)
public class UserContext {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 线程本地变量，用于存储当前登录用户信息
     */
    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();
    
    /**
     * 获取当前登录用户信息
     * 
     * @return 用户上下文
     */
    public static UserContext getCurrentUser() {
        return CONTEXT.get();
    }
    
    /**
     * 设置当前登录用户信息
     * 
     * @param userContext 用户上下文
     */
    public static void setCurrentUser(UserContext userContext) {
        CONTEXT.set(userContext);
    }
    
    /**
     * 清除当前登录用户信息
     */
    public static void clear() {
        CONTEXT.remove();
    }
} 