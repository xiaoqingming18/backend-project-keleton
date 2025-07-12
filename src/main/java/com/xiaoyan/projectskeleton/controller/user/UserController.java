package com.xiaoyan.projectskeleton.controller.user;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.user.JwtTokenDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserLoginDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserRegisterDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetRequestDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetVerifyDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileUpdateDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserAvatarUpdateDTO;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import com.xiaoyan.projectskeleton.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Validated UserRegisterDTO registerDTO) {
        log.info("用户注册: {}", registerDTO.getUsername());
        User user = userService.register(registerDTO);
        return ApiResponse.success(user, "注册成功");
    }
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果（JWT令牌）
     */
    @PostMapping("/login")
    public ApiResponse<JwtTokenDTO> login(@RequestBody @Validated UserLoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());
        JwtTokenDTO jwtTokenDTO = userService.login(loginDTO);
        return ApiResponse.success(jwtTokenDTO, "登录成功");
    }
    
    /**
     * 获取当前登录用户的资料
     * @return 用户资料
     */
    @GetMapping("/profile")
    @RequireLogin
    public ApiResponse<UserProfileDTO> getCurrentUserProfile() {
        log.info("获取当前登录用户的资料");
        UserProfileDTO profileDTO = userService.getCurrentUserProfile();
        return ApiResponse.success(profileDTO, "获取用户资料成功");
    }
    
    /**
     * 获取指定用户的资料
     * @param userId 用户ID
     * @return 用户资料
     */
    @GetMapping("/profile/{userId}")
    @RequireLogin
    public ApiResponse<UserProfileDTO> getUserProfile(@PathVariable Long userId) {
        log.info("获取用户资料: {}", userId);
        UserProfileDTO profileDTO = userService.getUserProfileById(userId);
        return ApiResponse.success(profileDTO, "获取用户资料成功");
    }
    
    /**
     * 更新当前登录用户的资料
     * @param updateDTO 更新资料请求
     * @return 更新后的用户资料
     */
    @PutMapping("/profile")
    @RequireLogin
    public ApiResponse<UserProfileDTO> updateUserProfile(@RequestBody @Validated UserProfileUpdateDTO updateDTO) {
        log.info("更新当前登录用户的资料");
        UserProfileDTO profileDTO = userService.updateCurrentUserProfile(updateDTO);
        return ApiResponse.success(profileDTO, "更新用户资料成功");
    }
    
    /**
     * 更新当前登录用户的头像
     * @param avatarFile 头像文件
     * @return 更新后的头像URL
     */
    @PostMapping("/avatar")
    @RequireLogin
    public ApiResponse<UserAvatarUpdateDTO> updateAvatar(@RequestParam("file") MultipartFile avatarFile) {
        log.info("更新当前登录用户的头像");
        UserAvatarUpdateDTO result = userService.updateCurrentUserAvatar(avatarFile);
        return ApiResponse.success(result, "头像更新成功");
    }
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check-username")
    public ApiResponse<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsernameExists(username);
        return ApiResponse.success(exists);
    }
    
    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.checkEmailExists(email);
        return ApiResponse.success(exists);
    }
    
    /**
     * 发送密码重置验证码
     * @param requestDTO 密码重置请求
     * @return 发送结果
     */
    @PostMapping("/password/reset-code")
    public ApiResponse<Void> sendPasswordResetCode(@RequestBody @Validated PasswordResetRequestDTO requestDTO) {
        log.info("发送密码重置验证码: {}", requestDTO.getEmail());
        userService.sendPasswordResetCode(requestDTO);
        return ApiResponse.success(null, "验证码已发送到您的邮箱，请注意查收");
    }
    
    /**
     * 验证验证码并重置密码
     * @param verifyDTO 验证信息
     * @return 重置结果
     */
    @PostMapping("/password/reset")
    public ApiResponse<Void> resetPassword(@RequestBody @Validated PasswordResetVerifyDTO verifyDTO) {
        log.info("验证验证码并重置密码: {}", verifyDTO.getEmail());
        userService.verifyCodeAndResetPassword(verifyDTO);
        return ApiResponse.success(null, "密码重置成功");
    }
} 