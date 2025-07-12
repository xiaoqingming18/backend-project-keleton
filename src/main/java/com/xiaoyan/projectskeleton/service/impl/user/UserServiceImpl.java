package com.xiaoyan.projectskeleton.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyan.projectskeleton.common.config.JwtConfig;
import com.xiaoyan.projectskeleton.common.context.UserContext;
import com.xiaoyan.projectskeleton.common.enums.UserStatusEnum;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.EmailErrorCode;
import com.xiaoyan.projectskeleton.common.exception.ExceptionUtils;
import com.xiaoyan.projectskeleton.common.exception.UserErrorCode;
import com.xiaoyan.projectskeleton.common.exception.FileErrorCode;
import com.xiaoyan.projectskeleton.common.util.JwtUtils;
import com.xiaoyan.projectskeleton.mapper.role.RoleMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserMapper;
import com.xiaoyan.projectskeleton.mapper.user.UserProfileMapper;
import com.xiaoyan.projectskeleton.repository.dto.file.FileUploadResponseDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.JwtTokenDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserLoginDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserRegisterDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetRequestDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.PasswordResetVerifyDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserProfileUpdateDTO;
import com.xiaoyan.projectskeleton.repository.dto.user.UserAvatarUpdateDTO;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import com.xiaoyan.projectskeleton.repository.entity.user.UserProfile;
import com.xiaoyan.projectskeleton.service.user.UserService;
import com.xiaoyan.projectskeleton.service.EmailService;
import com.xiaoyan.projectskeleton.service.file.FileService;
import com.xiaoyan.projectskeleton.common.util.EmailTemplateUtil;
import com.xiaoyan.projectskeleton.common.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private FileService fileService;
    
    /**
     * 默认角色编码
     */
    private static final String DEFAULT_ROLE_CODE = "USER";
    
    /**
     * 验证码有效期（秒）
     */
    @Value("${verification.code.expire-time:300}")
    private long verificationCodeExpireTime;
    
    /**
     * 验证码限流时间（秒）
     */
    @Value("${verification.code.limit-time:60}")
    private long verificationCodeLimitTime;
    
    /**
     * 验证码长度
     */
    @Value("${verification.code.length:6}")
    private int verificationCodeLength;
    
    /**
     * 验证码Redis前缀
     */
    private static final String PASSWORD_RESET_CODE_PREFIX = "password:reset:code:";
    
    /**
     * 验证码限流Redis前缀
     */
    private static final String PASSWORD_RESET_LIMIT_PREFIX = "password:reset:limit:";
    
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
            profileDTO.setRealName(userProfile.getRealName());
            profileDTO.setGender(userProfile.getGender());
            profileDTO.setBirthday(userProfile.getBirthday());
            profileDTO.setSignature(userProfile.getSignature());
            profileDTO.setAddress(userProfile.getAddress());
            profileDTO.setBio(userProfile.getBio());
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
        return userMapper.selectById(id);
    }
    
    /**
     * 封禁用户
     * @param userId 用户ID
     * @param reason 封禁原因（可选）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void banUser(Long userId, String reason) {
        // 1. 获取当前操作人信息
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXISTS, "用户未登录");
        }
        
        // 2. 获取用户信息
        User user = getById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 3. 防止管理员被封禁
        Role role = roleMapper.selectById(user.getRoleId());
        if (role != null && "ADMIN".equals(role.getCode())) {
            throw new BusinessException(UserErrorCode.OPERATION_NOT_ALLOWED, "不能封禁管理员账号");
        }
        
        // 4. 防止自己封禁自己
        if (userId.equals(userContext.getUserId())) {
            throw new BusinessException(UserErrorCode.OPERATION_NOT_ALLOWED, "不能封禁自己的账号");
        }
        
        // 5. 如果已经是封禁状态，则不重复封禁
        if (user.getStatus() == UserStatusEnum.BANNED.getId()) {
            throw new BusinessException(UserErrorCode.ACCOUNT_ALREADY_BANNED, "该用户已经处于封禁状态");
        }
        
        // 6. 修改用户状态为封禁
        user.setStatus(UserStatusEnum.BANNED.getId());
        userMapper.updateById(user);
        
        // 7. 记录封禁日志
        log.info("用户 {} 被 {} 封禁，原因：{}", user.getUsername(), userContext.getUsername(), reason);
        
        // 8. 发送封禁通知邮件
        try {
            // 生成封禁通知邮件内容
            String emailContent = EmailTemplateUtil.getBanNotificationTemplate(
                user.getUsername(),
                reason,
                "support@example.com", // 支持邮箱，可以配置到application.properties中
                "系统自动发送，请勿回复"
            );
            
            // 发送邮件
            emailService.sendHtmlEmail(
                null, // 使用系统默认发件人
                user.getEmail(),
                "账号封禁通知",
                emailContent
            );
            
            log.info("已向用户 {} 发送封禁通知邮件", user.getEmail());
        } catch (Exception e) {
            // 邮件发送失败不影响封禁操作，只记录日志
            log.error("向用户 {} 发送封禁通知邮件失败：{}", user.getEmail(), e.getMessage());
        }
    }
    
    /**
     * 解封用户
     * @param userId 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbanUser(Long userId) {
        // 1. 获取用户信息
        User user = getById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 2. 如果不是封禁状态，则不需要解封
        if (user.getStatus() != UserStatusEnum.BANNED.getId()) {
            throw new BusinessException(UserErrorCode.OPERATION_NOT_ALLOWED, "该用户不处于封禁状态");
        }
        
        // 3. 修改用户状态为正常
        user.setStatus(UserStatusEnum.NORMAL.getId());
        userMapper.updateById(user);
        
        // 4. 记录解封日志（如果需要的话，可以在此处添加记录解封日志的代码）
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext != null) {
            log.info("用户 {} 被 {} 解封", user.getUsername(), userContext.getUsername());
        }
    }
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @Override
    public List<UserProfileDTO> listAllUsers() {
        // 1. 查询所有未删除的用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDeleted, 0);
        List<User> userList = userMapper.selectList(queryWrapper);
        
        // 2. 转换为用户资料DTO
        if (userList == null || userList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return userList.stream().map(user -> getUserProfileById(user.getId()))
                .collect(Collectors.toList());
    }
    
    /**
     * 删除用户
     * @param userId 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 1. 获取当前操作人信息
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_EXISTS, "用户未登录");
        }
        
        // 2. 获取用户信息
        User user = getById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 3. 防止管理员被删除
        Role role = roleMapper.selectById(user.getRoleId());
        if (role != null && "ADMIN".equals(role.getCode())) {
            throw new BusinessException(UserErrorCode.OPERATION_NOT_ALLOWED, "不能删除管理员账号");
        }
        
        // 4. 防止自己删除自己
        if (userId.equals(userContext.getUserId())) {
            throw new BusinessException(UserErrorCode.OPERATION_NOT_ALLOWED, "不能删除自己的账号");
        }
        
        // 5. 逻辑删除用户（MyBatis-Plus的逻辑删除）
        userMapper.deleteById(userId);
        
        // 6. 记录删除日志
        log.info("用户 {} 被 {} 删除", user.getUsername(), userContext.getUsername());
    }
    
    /**
     * 禁用用户（设置为封禁状态）
     * @param userId 用户ID
     */
    @Override
    public void disableUser(Long userId) {
        banUser(userId, "管理员禁用操作");
    }
    
    /**
     * 启用用户（设置为正常状态）
     * @param userId 用户ID
     */
    @Override
    public void enableUser(Long userId) {
        // 1. 获取用户信息
        User user = getById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 2. 设置为正常状态
        user.setStatus(UserStatusEnum.NORMAL.getId());
        userMapper.updateById(user);
        
        // 3. 记录日志
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext != null) {
            log.info("用户 {} 被 {} 启用", user.getUsername(), userContext.getUsername());
        }
    }
    
    /**
     * 发送密码重置验证码
     * @param requestDTO 密码重置请求
     */
    @Override
    public void sendPasswordResetCode(PasswordResetRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        
        // 1. 检查邮箱是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        ExceptionUtils.assertNotNull(user, UserErrorCode.EMAIL_NOT_EXISTS);
        
        // 2. 检查是否频繁发送验证码
        String limitKey = PASSWORD_RESET_LIMIT_PREFIX + email;
        if (Boolean.TRUE.equals(redisUtils.hasKey(limitKey))) {
            throw new BusinessException(UserErrorCode.VERIFICATION_CODE_SEND_TOO_FREQUENTLY);
        }
        
        // 3. 生成随机验证码
        String code = generateVerificationCode(verificationCodeLength);
        
        // 4. 将验证码存入Redis，设置过期时间
        String codeKey = PASSWORD_RESET_CODE_PREFIX + email;
        redisUtils.set(codeKey, code, verificationCodeExpireTime);
        
        // 5. 设置发送限制
        redisUtils.set(limitKey, true, verificationCodeLimitTime);
        
        // 6. 发送验证码邮件
        try {
            String emailContent = EmailTemplateUtil.getPasswordResetCodeTemplate(
                user.getUsername(),
                code,
                verificationCodeExpireTime / 60 + "分钟",
                "系统自动发送，请勿回复"
            );
            
            emailService.sendHtmlEmail(
                null, // 使用系统默认发件人
                email,
                "密码修改验证码",
                emailContent
            );
            
            log.info("已向用户 {} 发送密码重置验证码", email);
        } catch (Exception e) {
            // 邮件发送失败，删除Redis中的验证码和限制
            redisUtils.delete(codeKey);
            redisUtils.delete(limitKey);
            
            log.error("向用户 {} 发送密码重置验证码失败：{}", email, e.getMessage());
            throw new BusinessException(EmailErrorCode.EMAIL_SEND_FAILED, "验证码发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 验证验证码并重置密码
     * @param verifyDTO 验证信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyCodeAndResetPassword(PasswordResetVerifyDTO verifyDTO) {
        // 1. 从DTO中获取邮箱
        String email = verifyDTO.getEmail();
        String code = verifyDTO.getCode();
        String newPassword = verifyDTO.getNewPassword();
        String confirmPassword = verifyDTO.getConfirmPassword();
        
        // 2. 检查新密码和确认密码是否一致
        ExceptionUtils.assertTrue(newPassword.equals(confirmPassword), 
                UserErrorCode.PASSWORD_NOT_MATCH);
        
        // 3. 检查验证码是否存在
        String codeKey = PASSWORD_RESET_CODE_PREFIX + email;
        Object savedCode = redisUtils.get(codeKey);
        if (savedCode == null) {
            throw new BusinessException(UserErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        
        // 4. 检查验证码是否正确
        if (!code.equals(savedCode.toString())) {
            throw new BusinessException(UserErrorCode.VERIFICATION_CODE_ERROR);
        }
        
        // 5. 根据邮箱查找用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 6. 更新密码
        // 实际项目中应该对密码进行加密处理
        user.setPassword(newPassword);
        userMapper.updateById(user);
        
        // 7. 删除Redis中的验证码
        redisUtils.delete(codeKey);
        
        log.info("用户 {} 成功重置密码", user.getUsername());
    }
    
    /**
     * 生成指定长度的随机验证码
     * @param length 验证码长度
     * @return 验证码
     */
    private String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * 更新当前登录用户的资料
     * @param updateDTO 更新资料请求
     * @return 更新后的用户资料
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileDTO updateCurrentUserProfile(UserProfileUpdateDTO updateDTO) {
        // 1. 获取当前登录用户信息
        UserContext userContext = UserContext.getCurrentUser();
        ExceptionUtils.assertNotNull(userContext, UserErrorCode.USER_NOT_LOGIN);
        
        Long userId = userContext.getUserId();
        
        // 2. 获取用户基本信息
        User user = userMapper.selectById(userId);
        ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
        
        // 3. 更新用户基本信息（仅限允许的字段）
        boolean userUpdated = false;
        if (StringUtils.hasText(updateDTO.getMobile())) {
            user.setMobile(updateDTO.getMobile());
            userUpdated = true;
        }
        
        if (userUpdated) {
            userMapper.updateById(user);
        }
        
        // 4. 获取或创建用户资料
        LambdaQueryWrapper<UserProfile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserProfile::getUserId, userId);
        UserProfile userProfile = userProfileMapper.selectOne(queryWrapper);
        
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
        }
        
        // 5. 更新用户资料（仅限允许的字段）
        boolean profileUpdated = false;
        
        if (StringUtils.hasText(updateDTO.getNickname())) {
            userProfile.setNickname(updateDTO.getNickname());
            profileUpdated = true;
        }
        
        if (StringUtils.hasText(updateDTO.getRealName())) {
            userProfile.setRealName(updateDTO.getRealName());
            profileUpdated = true;
        }
        
        if (updateDTO.getGender() != null) {
            userProfile.setGender(updateDTO.getGender());
            profileUpdated = true;
        }
        
        if (updateDTO.getBirthday() != null) {
            userProfile.setBirthday(updateDTO.getBirthday());
            profileUpdated = true;
        }
        
        if (StringUtils.hasText(updateDTO.getSignature())) {
            userProfile.setSignature(updateDTO.getSignature());
            profileUpdated = true;
        }
        
        if (StringUtils.hasText(updateDTO.getAddress())) {
            userProfile.setAddress(updateDTO.getAddress());
            profileUpdated = true;
        }
        
        if (StringUtils.hasText(updateDTO.getBio())) {
            userProfile.setBio(updateDTO.getBio());
            profileUpdated = true;
        }
        
        // 6. 保存用户资料
        if (profileUpdated) {
            if (userProfile.getId() == null) {
                userProfileMapper.insert(userProfile);
            } else {
                userProfileMapper.updateById(userProfile);
            }
        }
        
        // 7. 返回更新后的用户资料
        log.info("用户 {} 更新了个人资料", userContext.getUsername());
        return getUserProfileById(userId);
    }
    
    /**
     * 更新当前登录用户的头像
     * @param avatarFile 头像文件
     * @return 更新后的头像URL
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAvatarUpdateDTO updateCurrentUserAvatar(MultipartFile avatarFile) {
        // 1. 获取当前登录用户ID
        UserContext userContext = UserContext.getCurrentUser();
        ExceptionUtils.assertNotNull(userContext, UserErrorCode.USER_NOT_LOGIN);
        Long userId = userContext.getUserId();
        ExceptionUtils.assertNotNull(userId, UserErrorCode.USER_NOT_LOGIN);
        
        // 2. 校验文件是否为空
        if (avatarFile == null || avatarFile.isEmpty()) {
            throw new BusinessException(FileErrorCode.FILE_EMPTY);
        }
        
        // 3. 校验文件类型
        String contentType = avatarFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(FileErrorCode.FILE_TYPE_NOT_SUPPORTED, "只支持上传图片文件");
        }
        
        try {
            // 4. 上传头像文件到avatars目录
            FileUploadResponseDTO uploadResult = fileService.uploadFile(avatarFile, "avatars");
            
            // 5. 获取头像URL
            String avatarUrl = uploadResult.getUrl();
            
            // 6. 更新用户头像
            User user = userMapper.selectById(userId);
            ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
            
            // 7. 保存旧头像路径，用于后续删除
            String oldAvatarUrl = user.getAvatar();
            
            // 8. 更新用户头像URL
            user.setAvatar(avatarUrl);
            userMapper.updateById(user);
            
            // 9. 如果旧头像存在且不是默认头像，则删除旧头像
            if (StringUtils.hasText(oldAvatarUrl) && !oldAvatarUrl.contains("default-avatar")) {
                try {
                    // 从URL中提取对象名称
                    String objectName = oldAvatarUrl.substring(oldAvatarUrl.indexOf("avatars/"));
                    fileService.deleteFile(objectName);
                } catch (Exception e) {
                    // 删除旧头像失败不影响业务，只记录日志
                    log.warn("删除旧头像失败: {}", e.getMessage());
                }
            }
            
            // 10. 返回更新结果
            return UserAvatarUpdateDTO.builder()
                    .avatarUrl(avatarUrl)
                    .build();
            
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("更新用户头像失败: {}", e.getMessage(), e);
            throw new BusinessException(UserErrorCode.UPDATE_AVATAR_FAILED, "更新用户头像失败: " + e.getMessage());
        }
    }
} 