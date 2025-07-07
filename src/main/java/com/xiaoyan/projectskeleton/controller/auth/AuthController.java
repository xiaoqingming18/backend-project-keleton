package com.xiaoyan.projectskeleton.controller.auth;

import com.xiaoyan.projectskeleton.common.config.JwtConfig;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.CommonErrorCode;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.common.util.JwtUtils;
import com.xiaoyan.projectskeleton.repository.dto.user.JwtTokenDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.RefreshTokenDTO;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import com.xiaoyan.projectskeleton.service.role.RoleService;
import com.xiaoyan.projectskeleton.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtUtils jwtUtils;
    private final JwtConfig jwtConfig;
    private final UserService userService;
    private final RoleService roleService;
    
    /**
     * 刷新Token
     *
     * @param refreshTokenDTO 刷新Token请求
     * @return 新的Token
     */
    @PostMapping("/refresh-token")
    public ApiResponse<JwtTokenDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        String refreshToken = refreshTokenDTO.getRefreshToken();
        
        // 验证RefreshToken是否有效
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "RefreshToken已过期或无效");
        }
        
        // 从RefreshToken中获取用户ID
        Long userId = jwtUtils.getUserIdFromRefreshToken(refreshToken);
        
        // 获取用户信息
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "用户不存在");
        }
        
        // 获取角色信息
        Role role = roleService.getById(user.getRoleId());
        if (role == null) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "角色不存在");
        }
        
        // 生成新的AccessToken
        String newAccessToken = jwtUtils.generateAccessToken(user.getId(), user.getUsername(), role.getCode());
        
        // 生成新的RefreshToken
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getId());
        
        // 构建返回对象
        JwtTokenDTO jwtTokenDTO = JwtTokenDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessTokenExpiresIn(jwtConfig.getAccessTokenExpiration())
                .tokenType("Bearer")
                .build();
        
        return ApiResponse.success(jwtTokenDTO, "Token刷新成功");
    }
} 