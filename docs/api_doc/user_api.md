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