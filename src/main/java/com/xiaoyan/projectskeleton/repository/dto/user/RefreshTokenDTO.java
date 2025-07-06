package com.xiaoyan.projectskeleton.repository.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新Token的DTO
 */
@Data
public class RefreshTokenDTO {
    
    /**
     * 刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
} 