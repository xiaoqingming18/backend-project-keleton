# 业务异常工具

## 简介

业务异常工具是一套用于处理业务异常的工具类，提供了统一的异常处理机制，使业务代码更加简洁、清晰。

## 核心组件

### 1. BusinessException

业务异常基类，继承自 `RuntimeException`，用于表示业务异常。包含错误码和错误信息。

```java
// 使用错误码枚举创建业务异常
throw new BusinessException(UserErrorCode.USERNAME_ALREADY_EXISTS);

// 使用错误码和自定义错误信息创建业务异常
throw new BusinessException(UserErrorCode.USERNAME_ALREADY_EXISTS, "用户名 " + username + " 已存在");

// 使用自定义错误码和错误信息创建业务异常
throw new BusinessException(10001, "用户名已存在");
```

### 2. ErrorCode 接口

错误码接口，定义了获取错误码和错误信息的方法。

```java
public interface ErrorCode {
    Integer getCode();
    String getMessage();
}
```

### 3. 错误码枚举

#### CommonErrorCode

通用错误码枚举，定义了系统通用的错误码，如参数错误、未授权、服务器内部错误等。

```java
CommonErrorCode.PARAM_ERROR          // 400 参数错误
CommonErrorCode.UNAUTHORIZED         // 401 未授权
CommonErrorCode.FORBIDDEN            // 403 禁止访问
CommonErrorCode.NOT_FOUND            // 404 资源不存在
CommonErrorCode.INTERNAL_SERVER_ERROR // 500 服务器内部错误
CommonErrorCode.SERVICE_UNAVAILABLE   // 503 服务不可用
```

#### UserErrorCode

用户模块错误码枚举，定义了用户模块特有的错误码，如用户名已存在、密码不一致、邮箱已被注册等。

```java
UserErrorCode.USERNAME_ALREADY_EXISTS // 10001 用户名已存在
UserErrorCode.PASSWORD_NOT_MATCH      // 10002 两次输入的密码不一致
UserErrorCode.EMAIL_ALREADY_EXISTS    // 10003 邮箱已被注册
UserErrorCode.MOBILE_ALREADY_EXISTS   // 10004 手机号已被注册
UserErrorCode.USER_NOT_EXISTS         // 10005 用户不存在
UserErrorCode.PASSWORD_ERROR          // 10006 密码错误
UserErrorCode.ACCOUNT_DISABLED        // 10007 账号已被禁用
UserErrorCode.ACCOUNT_NOT_ACTIVATED   // 10008 账号未激活
UserErrorCode.ROLE_NOT_EXISTS         // 10009 角色不存在
```

### 4. ExceptionUtils

异常工具类，提供了一系列静态方法，用于方便地抛出业务异常和进行断言。

```java
// 抛出业务异常
ExceptionUtils.throwBizException(UserErrorCode.USERNAME_ALREADY_EXISTS);
ExceptionUtils.throwBizException(UserErrorCode.USERNAME_ALREADY_EXISTS, "用户名 " + username + " 已存在");
ExceptionUtils.throwBizException(10001, "用户名已存在");

// 断言为真，否则抛出业务异常
ExceptionUtils.assertTrue(password.equals(confirmPassword), UserErrorCode.PASSWORD_NOT_MATCH);

// 断言为假，否则抛出业务异常
ExceptionUtils.assertFalse(userService.checkUsernameExists(username), UserErrorCode.USERNAME_ALREADY_EXISTS);

// 断言对象不为空，否则抛出业务异常
ExceptionUtils.assertNotNull(user, UserErrorCode.USER_NOT_EXISTS);

// 断言对象为空，否则抛出业务异常
ExceptionUtils.assertNull(existingUser, UserErrorCode.USERNAME_ALREADY_EXISTS);
```

### 5. GlobalExceptionHandler

全局异常处理器，用于捕获并处理系统中的各类异常，将其转换为统一的响应格式返回给客户端。

- 处理业务异常 `BusinessException`
- 处理参数校验异常 `MethodArgumentNotValidException`
- 处理参数绑定异常 `BindException`
- 处理其他未知异常 `Exception`

## 使用示例

### 1. 在服务层抛出业务异常

```java
@Service
public class UserServiceImpl implements UserService {
    
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
}
```

### 2. 在控制器层处理业务异常

由于全局异常处理器的存在，控制器层无需手动处理业务异常，只需要调用服务层方法即可。

```java
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody @Validated UserRegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return ApiResponse.success(user, "注册成功");
    }
}
```

## 扩展

如果需要为其他模块添加错误码，只需要创建新的错误码枚举类，实现 `ErrorCode` 接口即可。例如：

```java
@Getter
public enum OrderErrorCode implements ErrorCode {
    
    ORDER_NOT_EXISTS(20001, "订单不存在"),
    ORDER_ALREADY_PAID(20002, "订单已支付"),
    ORDER_ALREADY_CANCELED(20003, "订单已取消"),
    INSUFFICIENT_INVENTORY(20004, "库存不足");
    
    private final Integer code;
    private final String message;
    
    OrderErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
``` 