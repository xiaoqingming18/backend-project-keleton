package com.xiaoyan.projectskeleton.controller.user;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.annotation.RequireRoles;
import com.xiaoyan.projectskeleton.common.enums.RoleEnum;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileDTO;
import com.xiaoyan.projectskeleton.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户管理相关功能，需要管理员权限
 */
@Slf4j
@RestController
@RequestMapping("/user/admin")
@RequireLogin
@RequireRoles(RoleEnum.ADMIN)
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;
    
    /**
     * 获取所有用户列表
     * 
     * @return 用户列表
     */
    @GetMapping("/list")
    public ApiResponse<List<UserProfileDTO>> listAllUsers() {
        log.info("获取所有用户列表");
        List<UserProfileDTO> userList = userService.listAllUsers();
        return ApiResponse.success(userList, "获取用户列表成功");
    }
    
    /**
     * 封禁用户
     * 
     * @param userId 用户ID
     * @param reason 封禁原因（可选）
     * @return 操作结果
     */
    @PutMapping("/{userId}/ban")
    public ApiResponse<Void> banUser(@PathVariable Long userId, @RequestParam(required = false) String reason) {
        log.info("封禁用户：{}，原因：{}", userId, reason);
        userService.banUser(userId, reason);
        return ApiResponse.success(null, "封禁用户成功");
    }
    
    /**
     * 解封用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/unban")
    public ApiResponse<Void> unbanUser(@PathVariable Long userId) {
        log.info("解封用户：{}", userId);
        userService.unbanUser(userId);
        return ApiResponse.success(null, "解封用户成功");
    }
    
    /**
     * 禁用用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/disable")
    public ApiResponse<Void> disableUser(@PathVariable Long userId) {
        log.info("禁用用户：{}", userId);
        userService.disableUser(userId);
        return ApiResponse.success(null, "禁用用户成功");
    }
    
    /**
     * 启用用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @PutMapping("/{userId}/enable")
    public ApiResponse<Void> enableUser(@PathVariable Long userId) {
        log.info("启用用户：{}", userId);
        userService.enableUser(userId);
        return ApiResponse.success(null, "启用用户成功");
    }
    
    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
        log.info("删除用户：{}", userId);
        userService.deleteUser(userId);
        return ApiResponse.success(null, "删除用户成功");
    }
} 