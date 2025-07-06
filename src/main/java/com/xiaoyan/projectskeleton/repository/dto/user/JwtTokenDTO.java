package com.xiaoyan.projectskeleton.repository.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT令牌DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDTO {
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 访问令牌过期时间（秒）
     */
    private Long accessTokenExpiresIn;
    
    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";
} 