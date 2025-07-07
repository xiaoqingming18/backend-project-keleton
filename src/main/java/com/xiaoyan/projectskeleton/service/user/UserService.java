package com.xiaoyan.projectskeleton.service.user;

import com.xiaoyan.projectskeleton.repository.dto.user.JwtTokenDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserLoginDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserRegisterDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetRequestDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetVerifyDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileUpdateDTO;
import com.xiaoyan.projectskeleton.repository.entity.user.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户
     */
    User register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return JWT令牌
     */
    JwtTokenDTO login(UserLoginDTO loginDTO);
    
    /**
     * 获取当前登录用户的资料
     * @return 用户资料
     */
    UserProfileDTO getCurrentUserProfile();
    
    /**
     * 根据用户ID获取用户资料
     * @param userId 用户ID
     * @return 用户资料
     */
    UserProfileDTO getUserProfileById(Long userId);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getById(Long id);
    
    /**
     * 封禁用户
     * @param userId 用户ID
     * @param reason 封禁原因（可选）
     */
    void banUser(Long userId, String reason);
    
    /**
     * 解封用户
     * @param userId 用户ID
     */
    void unbanUser(Long userId);
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    List<UserProfileDTO> listAllUsers();
    
    /**
     * 删除用户
     * @param userId 用户ID
     */
    void deleteUser(Long userId);
    
    /**
     * 禁用用户（设置为封禁状态）
     * @param userId 用户ID
     */
    void disableUser(Long userId);
    
    /**
     * 启用用户（设置为正常状态）
     * @param userId 用户ID
     */
    void enableUser(Long userId);
    
    /**
     * 发送密码重置验证码
     * @param requestDTO 密码重置请求
     */
    void sendPasswordResetCode(PasswordResetRequestDTO requestDTO);
    
    /**
     * 验证验证码并重置密码
     * @param verifyDTO 验证信息
     */
    void verifyCodeAndResetPassword(PasswordResetVerifyDTO verifyDTO);
    
    /**
     * 更新当前登录用户的资料
     * @param updateDTO 更新资料请求
     * @return 更新后的用户资料
     */
    UserProfileDTO updateCurrentUserProfile(UserProfileUpdateDTO updateDTO);
} 