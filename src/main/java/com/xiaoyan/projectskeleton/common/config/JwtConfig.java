package com.xiaoyan.projectskeleton.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    
    /**
     * JWT密钥
     */
    private String secret;
    
    /**
     * AccessToken过期时间（秒）
     */
    private Long accessTokenExpiration = 3600L; // 默认1小时
    
    /**
     * RefreshToken过期时间（秒）
     */
    private Long refreshTokenExpiration = 604800L; // 默认7天
    
    /**
     * Token签发者
     */
    private String issuer = "project-skeleton";
} 