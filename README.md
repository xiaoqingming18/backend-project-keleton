# Project Skeleton

## 项目介绍

这是一个基于 Spring Boot 3.2.4 和 Java 17 构建的项目骨架，提供了常见的后端服务结构和工具类。该项目旨在为开发者提供一个规范化的项目结构，包含了控制器、服务层、数据访问层等常用组件，以及通用的工具类和配置。

## 项目目录结构

```
src/main/java/com/xiaoyan/projectskeleton/
├── common/                 # 通用组件
│   ├── config/             # 配置类
│   │   ├── MybatisPlusConfig.java  # MyBatis-Plus配置类
│   │   ├── MyMetaObjectHandler.java # 字段自动填充处理器
│   │   ├── JwtConfig.java  # JWT配置类
│   │   └── WebMvcConfig.java # Web MVC配置类
│   ├── constant/           # 常量定义
│   ├── enums/              # 枚举类
│   ├── exception/          # 异常处理
│   ├── interceptor/        # 拦截器
│   └── util/               # 工具类
│       ├── ApiResponse.java # API响应封装工具类
│       └── JwtUtils.java    # JWT工具类
├── controller/             # 控制器层
│   └── auth/               # 认证相关控制器
│       └── AuthController.java # 认证控制器
├── service/                # 服务层接口
├── repository/             # 数据访问层
│   ├── dto/                # 数据传输对象
│   │   └── user/           # 用户相关DTO
│   │       ├── JwtTokenDTO.java # JWT令牌DTO
│   │       └── RefreshTokenDTO.java # 刷新令牌DTO
│   └── entity/             # 实体类
│       └── BaseEntity.java # 基础实体类
├── mapper/                 # MyBatis Mapper接口
└── ProjectSkeletonApplication.java # 应用程序入口
```

## 项目核心配置

项目的主要配置文件位于 `src/main/resources/application.properties`，包含以下配置项：

```properties
# 应用名称
spring.application.name=project-skeleton

# 数据库连接配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/skeleton?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456

# MyBatis-Plus配置
mybatis-plus.mapper-locations=classpath:mapper/**/*.xml
mybatis-plus.type-aliases-package=com.xiaoyan.projectskeleton.repository.entity
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis-plus.global-config.db-config.id-type=auto
mybatis-plus.global-config.db-config.logic-delete-field=deleted
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0

# JWT配置
jwt.secret=projectSkeletonSecretKey123456789012345678901234567890
jwt.access-token-expiration=3600
jwt.refresh-token-expiration=604800
jwt.issuer=project-skeleton
```

### Web MVC 配置

项目配置了所有接口统一使用 `/api` 前缀，配置位于 `src/main/java/com/xiaoyan/projectskeleton/common/config/WebMvcConfig.java`。

例如：
- `/user/list` 实际访问路径为 `/api/user/list`
- `/order/create` 实际访问路径为 `/api/order/create`

## 项目工具类

### ApiResponse

位置：`src/main/java/com/xiaoyan/projectskeleton/common/util/ApiResponse.java`

这是一个通用的 API 响应封装工具类，用于统一项目中所有接口的返回格式。响应格式包含以下字段：
- `code`：状态码（Integer类型）
- `success`：操作是否成功（Boolean类型）
- `message`：描述信息（String类型）
- `data`：返回数据（泛型T，可以是任何对象）

#### 使用方法

1. 成功响应，带数据和消息：
```java
ApiResponse<User> response = ApiResponse.success(user, "用户信息获取成功");
```

2. 失败响应，带错误码和消息：
```java
ApiResponse<Object> response = ApiResponse.failed(400, "参数错误");
```

### JwtUtils

位置：`src/main/java/com/xiaoyan/projectskeleton/common/util/JwtUtils.java`

这是一个JWT工具类，用于生成、验证和解析JWT令牌。主要功能包括：

1. 生成AccessToken和RefreshToken
2. 验证AccessToken和RefreshToken的有效性
3. 从AccessToken中获取用户信息（用户ID、用户名、角色）
4. 根据RefreshToken刷新AccessToken

#### 使用方法

1. 生成AccessToken：
```java
String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getUsername(), role.getCode());
```

2. 生成RefreshToken：
```java
String refreshToken = jwtUtils.generateRefreshToken(user.getId());
```

3. 从AccessToken中获取用户信息：
```java
Long userId = jwtUtils.getUserIdFromAccessToken(token);
String username = jwtUtils.getUsernameFromAccessToken(token);
String roleCode = jwtUtils.getRoleFromAccessToken(token);
```

4. 刷新AccessToken：
```java
String newAccessToken = jwtUtils.refreshAccessToken(refreshToken, user, roleCode);
```

### EmailService

位置：`src/main/java/com/xiaoyan/projectskeleton/service/EmailService.java`

这是一个邮件服务接口，提供了多种邮件发送功能。服务实现类位于`src/main/java/com/xiaoyan/projectskeleton/service/impl/EmailServiceImpl.java`。主要功能包括：

1. 发送纯文本邮件
2. 发送HTML格式邮件
3. 发送带附件的邮件
4. 完整的自定义邮件发送

#### 使用方法

1. 发送纯文本邮件：
```java
emailService.sendTextEmail("sender@example.com", "receiver@example.com", "邮件主题", "邮件内容");
```

2. 发送HTML邮件：
```java
emailService.sendHtmlEmail(null, "receiver@example.com", "HTML邮件", "<h1>这是一封HTML邮件</h1><p>邮件内容</p>");
```

3. 使用模板发送HTML邮件：
```java
String htmlContent = EmailTemplateUtil.getNotificationTemplate("系统通知", "您的账号已激活", "系统邮件，请勿回复");
emailService.sendHtmlEmail(null, "receiver@example.com", "账号激活通知", htmlContent);
```

### EmailTemplateUtil

位置：`src/main/java/com/xiaoyan/projectskeleton/common/util/EmailTemplateUtil.java`

这是一个邮件模板工具类，提供了多种预定义的HTML邮件模板。主要功能包括：

1. 简单HTML模板生成
2. 通知邮件模板生成
3. 验证码邮件模板生成
4. 带按钮的操作邮件模板生成
5. 参数化模板处理（动态替换模板中的变量）

#### 使用方法

1. 生成简单HTML模板：
```java
String htmlContent = EmailTemplateUtil.getSimpleTemplate("邮件标题", "邮件正文内容", "页脚信息");
```

2. 生成通知邮件模板：
```java
String htmlContent = EmailTemplateUtil.getNotificationTemplate("系统通知", "您的账号已激活", "系统邮件，请勿回复");
```

3. 生成验证码邮件模板：
```java
String htmlContent = EmailTemplateUtil.getVerificationCodeTemplate("123456", "10分钟", "系统邮件，请勿回复");
```

4. 生成带按钮的操作邮件模板：
```java
String content = "<p>请点击下方按钮激活您的账号</p>";
String htmlContent = EmailTemplateUtil.getButtonTemplate("账号激活", content, "立即激活", "https://example.com/activate", "系统邮件，请勿回复");
```

5. 参数化模板处理：
```java
String template = "尊敬的${username}，您好！您的验证码为：${code}，有效期为${validTime}。";
Map<String, String> params = new HashMap<>();
params.put("username", "张三");
params.put("code", "123456");
params.put("validTime", "10分钟");
String content = EmailTemplateUtil.processTemplate(template, params);
```

### JWT双Token认证方案

项目实现了基于JWT的双Token认证方案，包括AccessToken和RefreshToken：

1. **AccessToken**：
   - 用于访问受保护的API资源
   - 包含用户ID、用户名和角色信息
   - 有效期较短（默认1小时）

2. **RefreshToken**：
   - 用于在AccessToken过期后获取新的AccessToken
   - 仅包含用户ID
   - 有效期较长（默认7天）

#### 认证流程

1. 用户登录成功后，服务端生成AccessToken和RefreshToken返回给客户端
2. 客户端使用AccessToken访问受保护的API资源
3. 当AccessToken过期时，客户端使用RefreshToken请求刷新接口获取新的AccessToken和RefreshToken
4. 如果RefreshToken也过期，用户需要重新登录

#### 刷新Token接口

- 接口路径：`/api/auth/refresh-token`
- 请求方式：POST
- 请求参数：
  ```json
  {
    "refreshToken": "your_refresh_token"
  }
  ```
- 响应结果：
  ```json
  {
    "code": 200,
    "success": true,
    "message": "Token刷新成功",
    "data": {
      "accessToken": "new_access_token",
      "refreshToken": "new_refresh_token",
      "accessTokenExpiresIn": 3600,
      "tokenType": "Bearer"
    }
  }
  ```

### 接口访问控制机制

项目实现了基于JWT的接口访问控制机制，包括登录验证和角色权限验证。

#### 自定义注解

1. **@RequireLogin**

   位置：`src/main/java/com/xiaoyan/projectskeleton/common/annotation/RequireLogin.java`

   用于标记需要登录才能访问的接口。在控制器方法上添加此注解后，未登录用户将无法访问该接口。

   ```java
   @Target({ElementType.METHOD, ElementType.TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   public @interface RequireLogin {
   }
   ```

   使用示例：
   ```java
   @GetMapping("/profile")
   @RequireLogin
   public ApiResponse<UserProfileDTO> getUserProfile() {
       // 只有登录用户才能访问此接口
       Long userId = UserContext.getCurrentUserId();
       return ApiResponse.success(userService.getUserProfile(userId));
   }
   ```

2. **@RequireRoles**

   位置：`src/main/java/com/xiaoyan/projectskeleton/common/annotation/RequireRoles.java`

   用于标记需要特定角色才能访问的接口。在控制器方法上添加此注解并指定允许的角色后，只有拥有指定角色的用户才能访问该接口。
   
   注解使用枚举类型的参数，避免了角色编码拼写错误的问题。

   ```java
   @Target({ElementType.METHOD, ElementType.TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface RequireRoles {
       /**
        * 允许访问的角色枚举列表
        * 为空时表示不限制角色
        */
       RoleEnum[] value() default {};
       
       /**
        * 逻辑类型
        * 默认为OR，表示满足任一角色即可访问
        * 设置为AND时，表示需要同时满足所有角色才能访问
        */
       Logical logical() default Logical.OR;
       
       // 逻辑类型枚举定义...
   }
   ```

   使用示例：
   ```java
   @PutMapping("/admin/user/{id}")
   @RequireRoles(RoleEnum.ADMIN)
   public ApiResponse<Void> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
       // 只有ADMIN角色的用户才能访问此接口
       userService.updateUser(id, userUpdateDTO);
       return ApiResponse.success();
   }
   
   // 使用多个角色和逻辑类型的示例
   @DeleteMapping("/admin/user/{id}")
   @RequireRoles(value = {RoleEnum.ADMIN}, logical = RequireRoles.Logical.AND)
   public ApiResponse<Void> deleteUser(@PathVariable Long id) {
       // 必须同时拥有所有指定角色的用户才能访问
       userService.deleteUser(id);
       return ApiResponse.success();
   }
   ```

#### 用户上下文

位置：`src/main/java/com/xiaoyan/projectskeleton/common/context/UserContext.java`

用户上下文类，基于ThreadLocal实现，用于在请求处理过程中存储和获取当前登录用户的信息。

```java
public class UserContext {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> usernameHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> roleHolder = new ThreadLocal<>();

    // 设置当前用户ID
    public static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    // 获取当前用户ID
    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    // 设置当前用户名
    public static void setCurrentUsername(String username) {
        usernameHolder.set(username);
    }

    // 获取当前用户名
    public static String getCurrentUsername() {
        return usernameHolder.get();
    }

    // 设置当前用户角色
    public static void setCurrentRole(String role) {
        roleHolder.set(role);
    }

    // 获取当前用户角色
    public static String getCurrentRole() {
        return roleHolder.get();
    }

    // 清除当前用户信息
    public static void clear() {
        userIdHolder.remove();
        usernameHolder.remove();
        roleHolder.remove();
    }
}
```

#### 认证拦截器

位置：`src/main/java/com/xiaoyan/projectskeleton/common/interceptor/AuthInterceptor.java`

认证拦截器，用于拦截请求并进行身份验证和权限检查。主要功能包括：

1. 从请求头中提取AccessToken
2. 验证AccessToken的有效性
3. 解析AccessToken获取用户信息
4. 检查接口是否需要登录
5. 检查接口是否需要特定角色
6. 将用户信息存储到UserContext中

```java
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是处理器方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 获取类上的注解
        RequireLogin classRequireLogin = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
        RequireRoles classRequireRoles = handlerMethod.getBeanType().getAnnotation(RequireRoles.class);

        // 获取方法上的注解
        RequireLogin methodRequireLogin = method.getAnnotation(RequireLogin.class);
        RequireRoles methodRequireRoles = method.getAnnotation(RequireRoles.class);

        // 判断是否需要登录
        boolean needLogin = classRequireLogin != null || methodRequireLogin != null || classRequireRoles != null || methodRequireRoles != null;

        // 如果不需要登录，直接放行
        if (!needLogin) {
            return true;
        }

        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "未登录或token已过期");
        }

        // 去掉Bearer前缀
        token = token.substring(7);

        try {
            // 验证token
            if (!jwtUtils.validateAccessToken(token)) {
                throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "token已过期或无效");
            }

            // 从token中获取用户信息
            Long userId = jwtUtils.getUserIdFromAccessToken(token);
            String username = jwtUtils.getUsernameFromAccessToken(token);
            String role = jwtUtils.getRoleFromAccessToken(token);

            // 设置用户上下文
            UserContext.setCurrentUserId(userId);
            UserContext.setCurrentUsername(username);
            UserContext.setCurrentRole(role);

            // 判断是否需要特定角色
            if (methodRequireRoles != null || classRequireRoles != null) {
                RequireRoles requireRoles = methodRequireRoles != null ? methodRequireRoles : classRequireRoles;
                boolean hasRole = false;
                
                if (requireRoles.logical() == RequireRoles.Logical.OR) {
                    // 满足任一角色即可
                    hasRole = Arrays.stream(requireRoles.value())
                            .anyMatch(roleEnum -> roleEnum.getCode().equals(role));
                } else {
                    // 必须满足所有角色
                    hasRole = Arrays.stream(requireRoles.value())
                            .allMatch(roleEnum -> roleEnum.getCode().equals(role));
                }
                
                if (!hasRole) {
                    throw new BusinessException(CommonErrorCode.FORBIDDEN, "权限不足");
                }
            }

            return true;
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "token验证失败：" + e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清除用户上下文
        UserContext.clear();
    }
}
```

#### 拦截器配置

位置：`src/main/java/com/xiaoyan/projectskeleton/common/config/WebMvcConfig.java`

在WebMvcConfig中注册AuthInterceptor拦截器，并配置拦截路径。

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有/api/下的请求
                .excludePathPatterns(        // 排除不需要拦截的路径
                        "/api/user/register",
                        "/api/user/check-username",
                        "/api/user/check-email",
                        "/api/user/role/list",
                        "/api/auth/login",
                        "/api/auth/refresh-token"
                );
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> true);  // 为所有控制器添加/api前缀
    }
}
```

#### 使用示例

1. **需要登录才能访问的接口**：

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    @RequireLogin
    public ApiResponse<UserProfileDTO> getUserProfile() {
        Long userId = UserContext.getCurrentUserId();
        return ApiResponse.success(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    @RequireLogin
    public ApiResponse<Void> updateUserProfile(@RequestBody UserProfileDTO profileDTO) {
        Long userId = UserContext.getCurrentUserId();
        userService.updateUserProfile(userId, profileDTO);
        return ApiResponse.success();
    }
}
```

2. **需要特定角色才能访问的接口**：

```java
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    @RequireRoles({"ADMIN"})
    public ApiResponse<List<UserDTO>> getAllUsers() {
        // 只有ADMIN角色才能访问此接口
        return ApiResponse.success(userService.getAllUsers());
    }

    @DeleteMapping("/user/{id}")
    @RequireRoles({"ADMIN"})
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        // 只有ADMIN角色才能访问此接口
        userService.deleteUser(id);
        return ApiResponse.success();
    }
}
```

### BaseEntity

位置：`src/main/java/com/xiaoyan/projectskeleton/repository/entity/BaseEntity.java`

这是一个基础实体类，所有实体类都应该继承此类。包含以下字段：
- `id`：主键ID
- `createTime`：创建时间（自动填充）
- `updateTime`：更新时间（自动填充）
- `createBy`：创建人（自动填充）
- `updateBy`：更新人（自动填充）
- `deleted`：逻辑删除标记（0：未删除，1：已删除）
- `version`：版本号（乐观锁）

## 数据库配置

项目使用 MySQL 数据库，通过 MyBatis-Plus 进行数据访问。

### MyBatis-Plus 配置

位置：`src/main/java/com/xiaoyan/projectskeleton/common/config/MybatisPlusConfig.java`

MyBatis-Plus 配置类，包含以下功能：
- 分页插件：支持分页查询
- 乐观锁插件：支持乐观锁机制
- 防全表更新与删除插件：防止误操作导致全表更新或删除

### 字段自动填充处理器

位置：`src/main/java/com/xiaoyan/projectskeleton/common/config/MyMetaObjectHandler.java`

用于自动填充实体类中的特定字段，如创建时间、更新时间等。

### 乐观锁机制

项目中实现了基于 MyBatis-Plus 的乐观锁机制，用于处理并发操作时的数据一致性问题。

#### 实现方式

1. **实体类配置**：
   - 所有实体类继承 `BaseEntity`，包含 `@Version` 注解的 `version` 字段
   - 初始值为 1，每次更新自动加 1

2. **插件配置**：
   - 在 `MybatisPlusConfig` 中注册了 `OptimisticLockerInnerInterceptor` 插件

3. **工作原理**：
   - 当执行更新操作时，会自动检查 `version` 字段
   - 如果数据库中的 `version` 与当前实体的 `version` 不一致，表示数据已被其他操作修改
   - 此时更新会失败，抛出异常，防止数据覆盖

#### 使用方式

使用乐观锁时，只需要确保实体类继承了 `BaseEntity` 即可，无需额外代码：

```java
// 1. 查询实体
User user = userMapper.selectById(1L);  // 假设 version=1

// 2. 修改实体
user.setUsername("newUsername");

// 3. 更新实体
// 此时会自动检查version字段，并在更新时将version加1
userMapper.updateById(user);  // 成功更新后，数据库中 version=2
```

#### 并发冲突处理

当发生并发冲突时（即其他操作先一步修改了数据），可以通过以下方式处理：

```java
try {
    int rows = userMapper.updateById(user);
    if (rows == 0) {
        // 更新失败，说明数据已被修改
        // 重新获取最新数据
        user = userMapper.selectById(user.getId());
        // 进行必要的业务处理...
    }
} catch (Exception e) {
    // 处理异常
    log.error("数据更新失败，可能存在并发冲突", e);
}
```

## 项目服务模块

目前项目包含以下服务模块：

1. **基础框架**
   - Spring Boot 3.2.4 基础框架
   - RESTful API 接口规范
   - 统一接口前缀 `/api`

2. **通用工具**
   - API 响应封装
   - 业务异常处理工具
   - JWT 认证工具

3. **数据访问层**
   - MyBatis-Plus 3.5.9
   - MySQL 数据库支持
   - 基础实体类
   - 字段自动填充
   - 逻辑删除
   - 乐观锁
   - 分页功能

4. **用户服务模块**
   - 用户基础信息管理
   - 用户资料管理
   - 角色权限管理
   - 用户注册功能（支持角色选择）
   - 用户状态管理（封禁/解封/删除用户）

   接口列表：
   - `/user/register` - 用户注册
   - `/user/check-username` - 检查用户名是否已存在
   - `/user/check-email` - 检查邮箱是否已被注册
   - `/user/role/list` - 获取所有角色列表
   - `/user/profile` - 获取当前登录用户的资料
   
   管理员接口（需要ADMIN角色）：
   - `/user/admin/list` - 获取所有用户列表
   - `/user/admin/{userId}/disable` - 禁用用户
   - `/user/admin/{userId}/enable` - 启用用户
   - `/user/admin/{userId}/ban` - 封禁用户
   - `/user/admin/{userId}/unban` - 解封用户
   - `/user/admin/{userId}` - 删除用户

5. **邮件服务模块**
   - 支持纯文本邮件发送
   - 支持HTML格式邮件发送
   - 支持带附件邮件发送
   - 内置多种邮件模板
     - 简单通知模板
     - 验证码邮件模板
     - 带按钮的操作邮件模板
   - 参数化模板支持（支持动态替换模板中的变量）

6. **认证服务模块**
   - JWT 双 Token 认证方案
   - Token 刷新功能

   接口列表：
   - `/auth/refresh-token` - 刷新 Token

项目仍在开发中，后续将添加更多功能模块，如用户认证、权限管理等。

> 注：详细的接口文档请参考 `docs/api_doc/` 目录下的相关文件，如 `docs/api_doc/user_api.md`。

## 技术栈

- Java 17
- Spring Boot 3.2.4
- MyBatis-Plus 3.5.9
- JWT 0.11.5
- MySQL
- Maven

## 特别说明

### 角色枚举类

位置：`src/main/java/com/xiaoyan/projectskeleton/common/enums/RoleEnum.java`

项目使用枚举类型表示用户角色，避免使用字符串硬编码引起的潜在错误。角色枚举类包含了所有系统角色的定义，并提供了通过角色编码获取枚举值的工具方法。

```java
@Getter
public enum RoleEnum {
    
    /**
     * 管理员
     */
    ADMIN("ADMIN", "管理员"),
    
    /**
     * 普通用户
     */
    USER("USER", "普通用户");
    
    /**
     * 角色编码
     */
    private final String code;
    
    /**
     * 角色名称
     */
    private final String name;
    
    /**
     * 根据编码获取枚举值
     */
    public static RoleEnum getByCode(String code) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum;
            }
        }
        return null;
    }
}
```

#### 使用场景

1. **在@RequireRoles注解中使用**：
   ```java
   @RequireRoles(RoleEnum.ADMIN)
   ```

2. **在业务逻辑中判断角色**：
   ```java
   if (RoleEnum.ADMIN.getCode().equals(userContext.getRoleCode())) {
       // 管理员特有的逻辑
   }
   ```

3. **根据角色编码获取枚举值**：
   ```java
   String roleCode = userContext.getRoleCode();
   RoleEnum roleEnum = RoleEnum.getByCode(roleCode);
   ```

4. **获取角色相关信息**：
   ```java
   String roleName = RoleEnum.ADMIN.getName(); // "管理员"
   String roleCode = RoleEnum.USER.getCode();  // "USER"
   ```

### 业务异常处理工具

业务异常处理工具是一套用于处理业务异常的工具类，提供了统一的异常处理机制，使业务代码更加简洁、清晰。

#### 核心组件

1. **BusinessException**

   业务异常基类，继承自 `RuntimeException`，用于表示业务异常。包含错误码和错误信息。

   ```java
   // 使用错误码枚举创建业务异常
   throw new BusinessException(UserErrorCode.USERNAME_ALREADY_EXISTS);
   
   // 使用错误码和自定义错误信息创建业务异常
   throw new BusinessException(UserErrorCode.USERNAME_ALREADY_EXISTS, "用户名 " + username + " 已存在");
   
   // 使用自定义错误码和错误信息创建业务异常
   throw new BusinessException(10001, "用户名已存在");
   ```

2. **ErrorCode 接口**

   错误码接口，定义了获取错误码和错误信息的方法。

   ```java
   public interface ErrorCode {
       Integer getCode();
       String getMessage();
   }
   ```

3. **错误码枚举**

   - **CommonErrorCode**：通用错误码枚举，定义了系统通用的错误码
   - **UserErrorCode**：用户模块错误码枚举，定义了用户模块特有的错误码（以 100 开头）

4. **ExceptionUtils**

   异常工具类，提供了一系列静态方法，用于方便地抛出业务异常和进行断言。

   ```java
   // 断言为真，否则抛出业务异常
   ExceptionUtils.assertTrue(password.equals(confirmPassword), UserErrorCode.PASSWORD_NOT_MATCH);
   
   // 断言为假，否则抛出业务异常
   ExceptionUtils.assertFalse(userService.checkUsernameExists(username), UserErrorCode.USERNAME_ALREADY_EXISTS);
   
   // 断言对象不为空，否则抛出业务异常
   ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);
   ```

5. **GlobalExceptionHandler**

   全局异常处理器，用于捕获并处理系统中的各类异常，将其转换为统一的响应格式返回给客户端。

#### 错误码设计

1. **通用错误码**（HTTP 标准错误码）
   - 400：参数错误
   - 401：未授权
   - 403：禁止访问
   - 404：资源不存在
   - 500：服务器内部错误

2. **用户模块错误码**（以 100 开头）
   - 10001：用户名已存在
   - 10002：两次输入的密码不一致
   - 10003：邮箱已被注册
   - 10004：手机号已被注册
   - 10005：用户不存在
   - 10006：密码错误
   - 10007：账号已被禁用
   - 10008：账号未激活
   - 10009：角色不存在

#### 使用示例

在服务层使用异常工具类处理业务异常：

```java
@Override
public User register(UserRegisterDTO registerDTO) {
    // 校验用户名是否已存在
    ExceptionUtils.assertFalse(checkUsernameExists(registerDTO.getUsername()), 
            UserErrorCode.USERNAME_ALREADY_EXISTS);
    
    // 校验邮箱是否已存在
    ExceptionUtils.assertFalse(checkEmailExists(registerDTO.getEmail()), 
            UserErrorCode.EMAIL_ALREADY_EXISTS);
    
    // 校验密码是否一致
    ExceptionUtils.assertTrue(registerDTO.getPassword().equals(registerDTO.getConfirmPassword()), 
            UserErrorCode.PASSWORD_NOT_MATCH);
    
    // ... 其他业务逻辑
}
```

#### 扩展

如果需要为其他模块添加错误码，只需要创建新的错误码枚举类，实现 `ErrorCode` 接口即可，建议按模块划分错误码前缀：

- 用户模块：100xx
- 订单模块：200xx
- 商品模块：300xx
- 支付模块：400xx

#### HTTP错误处理

系统已配置全局异常处理器，可以处理以下HTTP错误：

- **404 Not Found**：当请求的接口不存在时，返回404错误码和友好提示
- **405 Method Not Allowed**：当请求方法不支持时，返回405错误码和支持的方法列表
- **400 Bad Request**：处理参数校验失败、参数类型不匹配、请求体解析错误等情况
- **500 Internal Server Error**：处理未预期的系统异常

相关配置位于 `application.properties`：
```properties
# 出现错误时抛出NoHandlerFoundException异常
spring.mvc.throw-exception-if-no-handler-found=true
# 禁用静态资源处理，防止静态资源被当作接口处理
spring.web.resources.add-mappings=false
```

### MyBatis-Plus 3.5.9 版本特性

从 MyBatis-Plus 3.5.9 版本开始，部分功能被拆分到单独的模块中：

1. **分页功能**: 需要额外引入 `mybatis-plus-jsqlparser` 依赖
   ```xml
   <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-jsqlparser</artifactId>
       <version>${mybatis-plus.version}</version>
   </dependency>
   ```

2. **配置分页插件**:
   ```java
   @Bean
   public MybatisPlusInterceptor mybatisPlusInterceptor() {
       MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
       
       // 分页插件
       PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
       paginationInnerInterceptor.setDbType(DbType.MYSQL);
       paginationInnerInterceptor.setMaxLimit(500L);
       interceptor.addInnerInterceptor(paginationInnerInterceptor);
       
       // 其他插件...
       
       return interceptor;
   }
   ``` 