package com.xiaoyan.projectskeleton.repository.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoyan.projectskeleton.repository.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 用户资料实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_profile")
public class UserProfile extends BaseEntity {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 昵称
     */
    private String nickname;
    
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