package com.xiaoyan.projectskeleton.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.config.JwtConfig;
import com.xiaoyan.projectskeleton.common.context.UserContext;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.ExceptionUtils;
import com.xiaoyan.projectskeleton.common.exception.UserErrorCode;
import com.xiaoyan.projectskeleton.common.util.JwtUtils;
import com.xiaoyan.projectskeleton.mapper.user.RoleMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserProfileMapper;
import com.xiaoyan.projectskeleton.repository.dto.user.JwtTokenDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserLoginDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserRegisterDTO;
import com.xiaoyan.projectskeleton.repository.entity.user.Role;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import com.xiaoyan.projectskeleton.repository.entity.user.UserProfile;
import com.xiaoyan.projectskeleton.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserProfileMapper userProfileMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private JwtConfig jwtConfig;
    
    /**
     * 默认角色编码
     */
    private static final String DEFAULT_ROLE_CODE = "USER";
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(UserRegisterDTO registerDTO) {
        // 1. 校验用户名和邮箱是否已存在
        ExceptionUtils.assertFalse(checkUsernameExists(registerDTO.getUsername()), 
                UserErrorCode.USERNAME_ALREADY_EXISTS);
        
        ExceptionUtils.assertFalse(checkEmailExists(registerDTO.getEmail()), 
                UserErrorCode.EMAIL_ALREADY_EXISTS);
        
        // 2. 校验密码是否一致
        ExceptionUtils.assertTrue(registerDTO.getPassword().equals(registerDTO.getConfirmPassword()), 
                UserErrorCode.PASSWORD_NOT_MATCH);
        
        // 3. 获取角色ID
        String roleCode = StringUtils.hasText(registerDTO.getUserRole()) ? 
                registerDTO.getUserRole() : DEFAULT_ROLE_CODE;
        Role role = roleMapper.findByCode(roleCode);
        if (role == null) {
            log.warn("角色编码 {} 不存在，使用默认角色", roleCode);
            role = roleMapper.findByCode(DEFAULT_ROLE_CODE);
            ExceptionUtils.assertNotNull(role, UserErrorCode.ROLE_NOT_EXISTS);
        }
        
        // 4. 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        // 实际项目中应该对密码进行加密处理
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());
        user.setMobile(registerDTO.getMobile());
        user.setStatus(0); // 未激活状态
        user.setRoleId(role.getId());
        
        // 5. 保存用户
        userMapper.insert(user);
        
        // 6. 创建用户资料
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        userProfile.setNickname(StringUtils.hasText(registerDTO.getNickname()) ? 
                registerDTO.getNickname() : registerDTO.getUsername());
        
        // 7. 保存用户资料
        userProfileMapper.insert(userProfile);
        
        return user;
    }
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return JWT令牌
     */
    @Override
    public JwtTokenDTO login(UserLoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        
        // 2. 校验用户是否存在
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 3. 校验密码是否正确（实际项目中应该对密码进行加密处理后再比较）
        ExceptionUtils.assertTrue(user.getPassword().equals(loginDTO.getPassword()), 
                UserErrorCode.PASSWORD_ERROR);
        
        // 4. 校验账号状态
        if (user.getStatus() == 0) {
            ExceptionUtils.throwBizException(UserErrorCode.ACCOUNT_NOT_ACTIVATED);
        } else if (user.getStatus() == 2) {
            ExceptionUtils.throwBizException(UserErrorCode.ACCOUNT_DISABLED);
        }
        
        // 5. 获取角色信息
        Role role = roleMapper.selectById(user.getRoleId());
        ExceptionUtils.assertNotNull(role, UserErrorCode.ROLE_NOT_EXISTS);
        
        // 6. 生成AccessToken和RefreshToken
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getUsername(), role.getCode());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId());
        
        // 7. 更新最后登录时间
        user.setLastLoginTime(java.time.LocalDateTime.now());
        userMapper.updateById(user);
        
        // 8. 返回JWT令牌
        return JwtTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(jwtConfig.getAccessTokenExpiration())
                .tokenType("Bearer")
                .build();
    }
    
    /**
     * 获取当前登录用户的资料
     * @return 用户资料
     */
    @Override
    public UserProfileDTO getCurrentUserProfile() {
        // 1. 获取当前登录用户信息
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXISTS, "用户未登录");
        }
        
        // 2. 获取用户资料
        return getUserProfileById(userContext.getUserId());
    }
    
    /**
     * 根据用户ID获取用户资料
     * @param userId 用户ID
     * @return 用户资料
     */
    @Override
    public UserProfileDTO getUserProfileById(Long userId) {
        // 1. 获取用户基本信息
        User user = userMapper.selectById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 2. 获取用户资料
        LambdaQueryWrapper<UserProfile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserProfile::getUserId, userId);
        UserProfile userProfile = userProfileMapper.selectOne(queryWrapper);
        
        // 3. 获取角色信息
        Role role = roleMapper.selectById(user.getRoleId());
        
        // 4. 构建用户资料DTO
        UserProfileDTO profileDTO = new UserProfileDTO();
        profileDTO.setUserId(user.getId());
        profileDTO.setUsername(user.getUsername());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setMobile(user.getMobile());
        profileDTO.setAvatar(user.getAvatar());
        profileDTO.setStatus(user.getStatus());
        
        // 设置最后登录时间
        if (user.getLastLoginTime() != null) {
            profileDTO.setLastLoginTime(user.getLastLoginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        // 设置角色信息
        if (role != null) {
            profileDTO.setRoleName(role.getName());
            profileDTO.setRoleCode(role.getCode());
        }
        
        // 设置用户资料信息
        if (userProfile != null) {
            profileDTO.setNickname(userProfile.getNickname());
        } else {
            profileDTO.setNickname(user.getUsername());
        }
        
        return profileDTO;
    }
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    @Override
    public boolean checkUsernameExists(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    @Override
    public boolean checkEmailExists(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public User getById(Long id) {
        return super.getById(id);
    }
} 