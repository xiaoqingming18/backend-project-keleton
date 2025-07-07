# 用户服务模块 API 文档

## 1. 用户注册接口

### 接口信息

- **接口路径**：`/user/register`
- **请求方式**：POST
- **接口描述**：用户注册接口，支持通过 `userRole` 参数指定用户角色

### 请求参数

#### 请求体 (JSON)

```json
{
  "username": "testuser",
  "password": "password123",
  "confirmPassword": "password123",
  "email": "test@example.com",
  "mobile": "13800138000",
  "nickname": "测试用户",
  "userRole": "USER"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | String | 是 | 用户名，4-20个字符，只能包含字母、数字、下划线和连字符 |
| password | String | 是 | 密码，6-20个字符 |
| confirmPassword | String | 是 | 确认密码，必须与password一致 |
| email | String | 是 | 邮箱地址 |
| mobile | String | 否 | 手机号码，格式为1开头的11位数字 |
| nickname | String | 否 | 昵称，不超过50个字符 |
| userRole | String | 否 | 用户角色编码，默认为"USER"，可选值：ADMIN、USER、GUEST |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "mobile": "13800138000",
    "status": 0,
    "roleId": 2,
    "createTime": "2023-07-06T16:30:00"
  }
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Object | 用户信息 |
| data.id | Long | 用户ID |
| data.username | String | 用户名 |
| data.email | String | 邮箱 |
| data.mobile | String | 手机号码 |
| data.status | Integer | 账号状态：0-未激活，1-正常，2-封禁 |
| data.roleId | Long | 角色ID |
| data.createTime | String | 创建时间 |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 10001 | 用户名已存在 |
| 10002 | 两次输入的密码不一致 |
| 10003 | 邮箱已被注册 |
| 10004 | 手机号已被注册 |
| 10005 | 用户不存在 |
| 10006 | 密码错误 |
| 10007 | 账号已被禁用 |
| 10008 | 账号未激活 |
| 10009 | 角色不存在 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. 用户登录接口

### 接口信息

- **接口路径**：`/user/login`
- **请求方式**：POST
- **接口描述**：用户登录接口，返回JWT令牌

### 请求参数

#### 请求体 (JSON)

```json
{
  "username": "testuser",
  "password": "password123"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "accessTokenExpiresIn": 3600,
    "tokenType": "Bearer"
  }
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Object | JWT令牌信息 |
| data.accessToken | String | 访问令牌 |
| data.refreshToken | String | 刷新令牌 |
| data.accessTokenExpiresIn | Long | 访问令牌过期时间（秒） |
| data.tokenType | String | 令牌类型，固定为"Bearer" |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 10005 | 用户不存在 |
| 10006 | 密码错误 |
| 10007 | 账号已被禁用 |
| 10008 | 账号未激活 |
| 10009 | 角色不存在 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 3. 用户名检查接口

### 接口信息

- **接口路径**：`/user/check-username`
- **请求方式**：GET
- **接口描述**：检查用户名是否已存在

### 请求参数

#### 查询参数

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| username | String | 是 | 要检查的用户名 |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "data": true  // true表示用户名已存在，false表示不存在
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Boolean | 用户名是否存在 |

## 4. 邮箱检查接口

### 接口信息

- **接口路径**：`/user/check-email`
- **请求方式**：GET
- **接口描述**：检查邮箱是否已被注册

### 请求参数

#### 查询参数

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| email | String | 是 | 要检查的邮箱 |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "data": true  // true表示邮箱已存在，false表示不存在
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Boolean | 邮箱是否存在 |

## 5. 角色列表接口

### 接口信息

- **接口路径**：`/user/role/list`
- **请求方式**：GET
- **接口描述**：获取所有角色列表

### 请求参数

无

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "获取角色列表成功",
  "data": [
    {
      "id": 1,
      "name": "超级管理员",
      "code": "ADMIN",
      "description": "系统超级管理员，拥有所有权限",
      "sort": 1,
      "createTime": "2023-07-06T16:30:00"
    },
    {
      "id": 2,
      "name": "普通用户",
      "code": "USER",
      "description": "普通用户，拥有基础权限",
      "sort": 2,
      "createTime": "2023-07-06T16:30:00"
    },
    {
      "id": 3,
      "name": "游客",
      "code": "GUEST",
      "description": "游客，只有查看权限",
      "sort": 3,
      "createTime": "2023-07-06T16:30:00"
    }
  ]
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Array | 角色列表 |
| data[].id | Long | 角色ID |
| data[].name | String | 角色名称 |
| data[].code | String | 角色编码 |
| data[].description | String | 角色描述 |
| data[].sort | Integer | 排序 |
| data[].createTime | String | 创建时间 | 

## 6. 获取当前登录用户资料接口

### 接口信息

- **接口路径**：`/user/profile`
- **请求方式**：GET
- **接口描述**：获取当前登录用户的资料信息
- **权限要求**：需要登录，不限制角色

### 请求参数

无

### 请求头

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| Authorization | String | 是 | 访问令牌，格式为"Bearer {accessToken}" |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "获取用户资料成功",
  "data": {
    "userId": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "email": "test@example.com",
    "mobile": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "roleName": "普通用户",
    "roleCode": "USER",
    "status": 1,
    "lastLoginTime": "2023-07-06 16:30:00"
  }
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Object | 用户资料信息 |
| data.userId | Long | 用户ID |
| data.username | String | 用户名 |
| data.nickname | String | 昵称 |
| data.email | String | 邮箱 |
| data.mobile | String | 手机号码 |
| data.avatar | String | 头像URL |
| data.roleName | String | 角色名称 |
| data.roleCode | String | 角色编码 |
| data.status | Integer | 账号状态：0-未激活，1-正常，2-封禁 |
| data.lastLoginTime | String | 最后登录时间 |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 10005 | 用户不存在 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 7. 获取指定用户资料接口

### 接口信息

- **接口路径**：`/user/profile/{userId}`
- **请求方式**：GET
- **接口描述**：获取指定用户的资料信息
- **权限要求**：需要登录，不限制角色

### 请求参数

#### 路径参数

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| userId | Long | 是 | 用户ID |

### 请求头

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| Authorization | String | 是 | 访问令牌，格式为"Bearer {accessToken}" |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "获取用户资料成功",
  "data": {
    "userId": 1,
    "username": "testuser",
    "nickname": "测试用户",
    "email": "test@example.com",
    "mobile": "13800138000",
    "avatar": "https://example.com/avatar.jpg",
    "roleName": "普通用户",
    "roleCode": "USER",
    "status": 1,
    "lastLoginTime": "2023-07-06 16:30:00"
  }
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Object | 用户资料信息 |
| data.userId | Long | 用户ID |
| data.username | String | 用户名 |
| data.nickname | String | 昵称 |
| data.email | String | 邮箱 |
| data.mobile | String | 手机号码 |
| data.avatar | String | 头像URL |
| data.roleName | String | 角色名称 |
| data.roleCode | String | 角色编码 |
| data.status | Integer | 账号状态：0-未激活，1-正常，2-封禁 |
| data.lastLoginTime | String | 最后登录时间 |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 10005 | 用户不存在 |
| 401 | 未授权 |
| 500 | 服务器内部错误 |

## 管理员操作接口

以下接口需要管理员权限，只有角色为ADMIN的用户才能访问。

### 1. 获取所有用户列表

- **接口路径**: `/api/user/admin/list`
- **请求方式**: GET
- **接口说明**: 获取系统中所有用户的信息列表
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "获取用户列表成功",
  "data": [
    {
      "userId": 1,
      "username": "admin",
      "email": "admin@example.com",
      "mobile": "13800138000",
      "nickname": "系统管理员",
      "avatar": "https://example.com/avatar.jpg",
      "status": 1,
      "lastLoginTime": "2023-07-01 12:30:45",
      "roleName": "管理员",
      "roleCode": "ADMIN"
    },
    {
      "userId": 2,
      "username": "user",
      "email": "user@example.com",
      "mobile": "13900139000",
      "nickname": "普通用户",
      "avatar": null,
      "status": 1,
      "lastLoginTime": "2023-07-02 10:20:30",
      "roleName": "普通用户",
      "roleCode": "USER"
    }
  ]
}
```

### 2. 禁用用户

- **接口路径**: `/api/user/admin/{userId}/disable`
- **请求方式**: PUT
- **接口说明**: 将用户状态修改为"封禁"状态，使其无法登录系统
- **路径参数**: 
  - `userId`: 用户ID，必填
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "禁用用户成功",
  "data": null
}
```

### 3. 启用用户

- **接口路径**: `/api/user/admin/{userId}/enable`
- **请求方式**: PUT
- **接口说明**: 将用户状态修改为"正常"状态，使其可以正常登录系统
- **路径参数**: 
  - `userId`: 用户ID，必填
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "启用用户成功",
  "data": null
}
```

### 4. 封禁用户

- **接口路径**: `/api/user/admin/{userId}/ban`
- **请求方式**: PUT
- **接口说明**: 封禁用户，可以指定封禁原因
- **路径参数**: 
  - `userId`: 用户ID，必填
- **查询参数**:
  - `reason`: 封禁原因，可选
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "封禁用户成功",
  "data": null
}
```

### 5. 解封用户

- **接口路径**: `/api/user/admin/{userId}/unban`
- **请求方式**: PUT
- **接口说明**: 解除用户的封禁状态
- **路径参数**: 
  - `userId`: 用户ID，必填
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "解封用户成功",
  "data": null
}
```

### 6. 删除用户

- **接口路径**: `/api/user/admin/{userId}`
- **请求方式**: DELETE
- **接口说明**: 删除指定用户（逻辑删除）
- **路径参数**: 
  - `userId`: 用户ID，必填
- **请求头**: 需要在Authorization头中携带有效的Token，且用户角色必须是ADMIN

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "删除用户成功",
  "data": null
}
```

## 错误码说明

| 错误码 | 说明 |
| ------ | ---- |
| 400 | 参数错误 |
| 401 | 未授权，未登录或Token已过期 |
| 403 | 权限不足 |
| 10001 | 用户名已存在 |
| 10002 | 两次输入的密码不一致 |
| 10003 | 邮箱已被注册 |
| 10004 | 手机号已被注册 |
| 10005 | 用户不存在 |
| 10006 | 密码错误 |
| 10007 | 账号已被禁用 |
| 10008 | 账号未激活 |
| 10010 | 操作不允许 |
| 10011 | 账号已被封禁 |
| 10012 | 无权限进行此操作 |
| 10013 | 验证码不存在或已过期 |
| 10014 | 验证码错误 |
| 10015 | 邮箱不存在 |
| 10016 | 验证码发送过于频繁 |
| 50001 | 邮件发送失败 |

## 密码重置接口

### 1. 发送密码重置验证码

- **接口路径**: `/api/user/password/reset-code`
- **请求方式**: POST
- **接口说明**: 发送密码重置验证码到用户邮箱，验证码有效期为5分钟

**请求参数**:

```json
{
  "email": "example@example.com"
}
```

| 参数名 | 类型   | 必填 | 说明     |
| ------ | ------ | ---- | -------- |
| email  | String | 是   | 用户邮箱 |

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "验证码已发送到您的邮箱，请注意查收",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
| ------ | ---- |
| 10015 | 邮箱不存在 |
| 10016 | 验证码发送过于频繁，请稍后再试 |
| 50001 | 邮件发送失败 |

**注意事项**:
- 同一邮箱在配置的限制时间内只能发送一次验证码（默认1分钟）
- 验证码有效期为配置的过期时间（默认5分钟）
- 邮箱必须是已注册的邮箱

### 2. 验证验证码并重置密码

- **接口路径**: `/api/user/password/reset`
- **请求方式**: POST
- **接口说明**: 验证用户提供的验证码，并重置用户密码

**请求参数**:

```json
{
  "email": "example@example.com",
  "code": "123456",
  "newPassword": "password123",
  "confirmPassword": "password123"
}
```

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| email | String | 是 | 用户邮箱 |
| code | String | 是 | 验证码 |
| newPassword | String | 是 | 新密码，6-20个字符 |
| confirmPassword | String | 是 | 确认新密码，必须与newPassword一致 |

**响应示例**:

```json
{
  "code": 200,
  "success": true,
  "message": "密码重置成功",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
| ------ | ---- |
| 10002 | 两次输入的密码不一致 |
| 10013 | 验证码不存在或已过期 |
| 10014 | 验证码错误 |
| 10015 | 邮箱不存在 |

**注意事项**:
- 验证码验证成功后会被立即删除，不能重复使用
- 新密码长度必须在6-20个字符之间
- 新密码和确认密码必须一致 