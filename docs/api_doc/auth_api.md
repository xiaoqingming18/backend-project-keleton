# 认证服务接口文档

## 刷新Token

### 接口描述

用于在AccessToken过期后，使用RefreshToken获取新的AccessToken和RefreshToken。

### 请求URL

```
POST /api/auth/refresh-token
```

### 请求参数

| 参数名 | 类型 | 是否必须 | 描述 |
| --- | --- | --- | --- |
| refreshToken | String | 是 | 刷新令牌 |

### 请求示例

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaXNzIjoicHJvamVjdC1za2VsZXRvbiIsImlhdCI6MTYyMDIwMjAyMCwiZXhwIjoxNjIwODA2ODIwfQ.3f8c_Rz4pOD_tX8JQxgxzRCrwWmGfRj-4ZGFgsBx-xQ"
}
```

### 响应参数

| 参数名 | 类型 | 描述 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 描述信息 |
| data | Object | 响应数据 |
| data.accessToken | String | 新的访问令牌 |
| data.refreshToken | String | 新的刷新令牌 |
| data.accessTokenExpiresIn | Long | 访问令牌过期时间（秒） |
| data.tokenType | String | 令牌类型，固定为"Bearer" |

### 响应示例

```json
{
  "code": 200,
  "success": true,
  "message": "Token刷新成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlIjoiQURNSU4iLCJpc3MiOiJwcm9qZWN0LXNrZWxldG9uIiwiaWF0IjoxNjIwMjAyMDIwLCJleHAiOjE2MjAyMDU2MjB9.8J7vK8_zt_tR6Z9ZmJLLmSskLpaKZl5yz2_PE8yrLdY",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaXNzIjoicHJvamVjdC1za2VsZXRvbiIsImlhdCI6MTYyMDIwMjAyMCwiZXhwIjoxNjIwODA2ODIwfQ.3f8c_Rz4pOD_tX8JQxgxzRCrwWmGfRj-4ZGFgsBx-xQ",
    "accessTokenExpiresIn": 3600,
    "tokenType": "Bearer"
  }
}
```

### 错误码

| 错误码 | 描述 |
| --- | --- |
| 401 | RefreshToken已过期或无效 |
| 401 | 用户不存在 |
| 401 | 角色不存在 |
| 401 | RefreshToken与用户不匹配 |

## JWT使用指南

### AccessToken

AccessToken用于访问受保护的API资源，包含以下信息：
- 用户ID
- 用户名
- 用户角色

有效期默认为1小时（3600秒），可通过配置文件修改。

### RefreshToken

RefreshToken用于在AccessToken过期后获取新的AccessToken，仅包含用户ID信息。

有效期默认为7天（604800秒），可通过配置文件修改。

### 认证流程

1. 用户登录成功后，服务端生成AccessToken和RefreshToken返回给客户端
2. 客户端将AccessToken添加到请求头中，格式为：`Authorization: Bearer {accessToken}`
3. 服务端验证AccessToken的有效性，并从中获取用户信息
4. 当AccessToken过期时，客户端使用RefreshToken请求刷新接口获取新的AccessToken和RefreshToken
5. 如果RefreshToken也过期，用户需要重新登录

### 配置说明

JWT相关配置位于`application.properties`文件中：

```properties
# JWT密钥（生产环境应使用更复杂的密钥并通过环境变量注入）
jwt.secret=projectSkeletonSecretKey123456789012345678901234567890
# AccessToken过期时间（秒）
jwt.access-token-expiration=3600
# RefreshToken过期时间（秒）
jwt.refresh-token-expiration=604800
# Token签发者
jwt.issuer=project-skeleton
``` 