# 角色模块 API 文档

## 1. 角色列表接口

### 接口信息

- **接口路径**：`/api/role/list`
- **请求方式**：GET
- **接口描述**：获取所有角色列表
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
  "message": "获取角色列表成功",
  "data": [
    {
      "id": 1,
      "name": "管理员",
      "code": "ADMIN",
      "description": "系统管理员，拥有所有权限",
      "sort": 1,
      "createTime": "2023-07-06T16:30:00"
    },
    {
      "id": 2,
      "name": "普通用户",
      "code": "USER",
      "description": "普通注册用户，拥有基础权限",
      "sort": 2,
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

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 401 | 未授权，未登录或Token已过期 |
| 500 | 服务器内部错误 |

## 2. 创建角色接口

### 接口信息

- **接口路径**：`/api/role/create`
- **请求方式**：POST
- **接口描述**：创建新角色
- **权限要求**：需要登录，且用户角色必须是ADMIN

### 请求参数

#### 请求体 (JSON)

```json
{
  "name": "运营人员",
  "code": "OPERATOR",
  "description": "系统运营人员，拥有部分管理权限",
  "sort": 3
}
```

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| name | String | 是 | 角色名称，最大长度50个字符 |
| code | String | 是 | 角色编码，只能包含大写字母和下划线，最大长度50个字符 |
| description | String | 否 | 角色描述，最大长度255个字符 |
| sort | Integer | 否 | 排序，默认为0 |

### 请求头

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| Authorization | String | 是 | 访问令牌，格式为"Bearer {accessToken}" |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "创建角色成功",
  "data": {
    "id": 3,
    "name": "运营人员",
    "code": "OPERATOR",
    "description": "系统运营人员，拥有部分管理权限",
    "sort": 3,
    "createTime": "2023-07-06T16:30:00"
  }
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Object | 创建后的角色信息 |
| data.id | Long | 角色ID |
| data.name | String | 角色名称 |
| data.code | String | 角色编码 |
| data.description | String | 角色描述 |
| data.sort | Integer | 排序 |
| data.createTime | String | 创建时间 |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 20001 | 角色编码已存在 |
| 20002 | 角色名称已存在 |
| 401 | 未授权，未登录或Token已过期 |
| 403 | 权限不足，非管理员用户 |
| 400 | 参数错误 |
| 500 | 服务器内部错误 |

## 3. 检查角色编码接口

### 接口信息

- **接口路径**：`/api/role/check-code`
- **请求方式**：GET
- **接口描述**：检查角色编码是否已存在
- **权限要求**：需要登录，不限制角色

### 请求参数

#### 查询参数

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| code | String | 是 | 要检查的角色编码 |

### 请求头

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| Authorization | String | 是 | 访问令牌，格式为"Bearer {accessToken}" |

### 响应参数

```json
{
  "code": 200,
  "success": true,
  "message": "操作成功",
  "data": true  // true表示角色编码已存在，false表示不存在
}
```

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| code | Integer | 状态码，200表示成功 |
| success | Boolean | 是否成功 |
| message | String | 提示信息 |
| data | Boolean | 角色编码是否存在 |

### 错误码

| 错误码 | 说明 |
| --- | --- |
| 401 | 未授权，未登录或Token已过期 |
| 500 | 服务器内部错误 | 