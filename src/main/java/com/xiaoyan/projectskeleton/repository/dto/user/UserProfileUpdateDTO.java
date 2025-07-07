package com.xiaoyan.projectskeleton.repository.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 用户资料更新DTO
 */
@Data
public class UserProfileUpdateDTO {
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
    
    /**
     * 手机号码
     */
    @Pattern(regexp = "^1\\d{10}$", message = "手机号码格式不正确")
    private String mobile;
    
    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    /**
     * 个性签名
     */
    @Size(max = 255, message = "个性签名长度不能超过255个字符")
    private String signature;
    
    /**
     * 地址
     */
    @Size(max = 255, message = "地址长度不能超过255个字符")
    private String address;
    
    /**
     * 个人简介
     */
    @Size(max = 1000, message = "个人简介长度不能超过1000个字符")
    private String bio;
} 