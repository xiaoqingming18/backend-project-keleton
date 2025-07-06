package com.xiaoyan.projectskeleton.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.mapper.user.RoleMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserProfileMapper;
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
    public ApiResponse<User> register(UserRegisterDTO registerDTO) {
        // 1. 校验用户名和邮箱是否已存在
        if (checkUsernameExists(registerDTO.getUsername())) {
            return ApiResponse.failed(400, "用户名已存在");
        }
        if (checkEmailExists(registerDTO.getEmail())) {
            return ApiResponse.failed(400, "邮箱已被注册");
        }
        
        // 2. 校验密码是否一致
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ApiResponse.failed(400, "两次输入的密码不一致");
        }
        
        // 3. 获取角色ID
        String roleCode = StringUtils.hasText(registerDTO.getUserRole()) ? 
                registerDTO.getUserRole() : DEFAULT_ROLE_CODE;
        Role role = roleMapper.findByCode(roleCode);
        if (role == null) {
            log.warn("角色编码 {} 不存在，使用默认角色", roleCode);
            role = roleMapper.findByCode(DEFAULT_ROLE_CODE);
            if (role == null) {
                return ApiResponse.failed(500, "系统错误：默认角色不存在");
            }
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
        
        return ApiResponse.success(user, "注册成功");
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
} 