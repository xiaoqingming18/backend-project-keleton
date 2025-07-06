package com.xiaoyan.projectskeleton.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.exception.ExceptionUtils;
import com.xiaoyan.projectskeleton.common.exception.UserErrorCode;
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