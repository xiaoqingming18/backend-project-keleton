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
│   │   └── WebMvcConfig.java # Web MVC配置类
│   ├── constant/           # 常量定义
│   ├── enums/              # 枚举类
│   ├── exception/          # 异常处理
│   ├── interceptor/        # 拦截器
│   └── util/               # 工具类
│       └── ApiResponse.java # API响应封装工具类
├── controller/             # 控制器层
├── service/                # 服务层接口
├── repository/             # 数据访问层
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

项目仍在开发中，后续将添加更多功能模块，如用户认证、权限管理等。

## 技术栈

- Java 17
- Spring Boot 3.2.4
- MyBatis-Plus 3.5.9
- MySQL
- Maven

## 特别说明

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