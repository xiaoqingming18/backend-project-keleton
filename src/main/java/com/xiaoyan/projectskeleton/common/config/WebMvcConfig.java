package com.xiaoyan.projectskeleton.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置接口统一前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 为所有接口路径添加 /api 前缀
        configurer.addPathPrefix("/api", clazz -> true);
    }
} 