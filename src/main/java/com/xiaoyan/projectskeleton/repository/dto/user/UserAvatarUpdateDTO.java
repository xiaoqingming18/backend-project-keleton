package com.xiaoyan.projectskeleton.repository.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户头像更新DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAvatarUpdateDTO {
    
    /**
     * 头像URL
     */
    private String avatarUrl;
} 