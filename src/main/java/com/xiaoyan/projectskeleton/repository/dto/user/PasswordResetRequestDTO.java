package com.xiaoyan.projectskeleton.repository.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 密码重置请求DTO
 */
@Data
public class PasswordResetRequestDTO {
    
    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
} 