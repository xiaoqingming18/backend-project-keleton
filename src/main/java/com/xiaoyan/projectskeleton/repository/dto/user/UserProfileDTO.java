package com.xiaoyan.projectskeleton.repository.dto.user;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户资料DTO
 */
@Data
public class UserProfileDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 账号状态：0-未激活，1-正常，2-封禁
     */
    private Integer status;
    
    /**
     * 最后登录时间
     */
    private String lastLoginTime;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDate birthday;
    
    /**
     * 个性签名
     */
    private String signature;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 个人简介
     */
    private String bio;
} 