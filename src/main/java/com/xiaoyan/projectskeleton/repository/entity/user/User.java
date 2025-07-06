package com.xiaoyan.projectskeleton.repository.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyan.projectskeleton.repository.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（加密存储）
     */
    private String password;
    
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
     * 账号状态：0-未激活，1-正常，2-封禁
     */
    private Integer status;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 角色ID
     */
    private Long roleId;
} 