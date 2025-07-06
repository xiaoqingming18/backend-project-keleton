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

   接口列表：
   - `/user/register` - 用户注册
   - `/user/check-username` - 检查用户名是否已存在
   - `/user/check-email` - 检查邮箱是否已被注册
   - `/user/role/list` - 获取所有角色列表

5. **认证服务模块**
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