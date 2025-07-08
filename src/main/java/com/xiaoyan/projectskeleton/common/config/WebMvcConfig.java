package com.xiaoyan.projectskeleton.common.config;

import com.xiaoyan.projectskeleton.common.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    
    /**
     * 配置接口统一前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 为所有接口路径添加 /api 前缀
        configurer.addPathPrefix("/api", clazz -> true);
    }
    
    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") // 拦截所有API请求
                .excludePathPatterns(
                        "/api/user/login",      // 登录接口
                        "/api/user/register",   // 注册接口
                        "/api/user/check-username", // 检查用户名接口
                        "/api/user/check-email",    // 检查邮箱接口
                        "/api/auth/refresh-token",  // 刷新Token接口
                        "/api/user/role/list",      // 角色列表接口
                        "/api/public/**"            // 公共接口，不需要登录即可访问
                );
    }
} 