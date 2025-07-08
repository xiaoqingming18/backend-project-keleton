# 权限管理API文档

## 1. 创建权限

### 接口说明

创建新的权限，需要管理员权限。

### 请求信息

- 请求方法: POST
- 请求路径: `/api/permission/create`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名    | 类型   | 必填 | 说明                                   |
|-----------|--------|------|----------------------------------------|
| name      | String | 是   | 权限名称，不能为空，最大长度50个字符   |
| code      | String | 是   | 权限编码，只能包含大写字母和下划线，最大长度50个字符 |
| type      | Integer| 是   | 权限类型：1-菜单，2-按钮，3-接口       |
| parentId  | Long   | 否   | 父权限ID，如果是顶级权限则为0，默认为0 |
| path      | String | 否   | 权限路径，如果是菜单则为前端路由路径，如果是接口则为后端接口路径 |
| icon      | String | 否   | 图标，仅对菜单类型有效                 |
| sort      | Integer| 否   | 排序，默认为0                          |

### 请求示例

```json
{
  "name": "用户管理",
  "code": "USER_MANAGE",
  "type": 1,
  "parentId": 0,
  "path": "/user",
  "icon": "user",
  "sort": 1
}
```

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Long   | 权限ID   |

### 响应示例

```json
{
  "code": 0,
  "message": "创建权限成功",
  "data": 1
}
```

### 错误码

| 错误码 | 说明             |
|--------|------------------|
| 30001  | 权限编码已存在   |
| 30002  | 权限名称已存在   |
| 30004  | 父权限不存在     |
| 30007  | 权限类型不正确   |

## 2. 获取权限树

### 接口说明

获取所有权限的树形结构，需要管理员权限。

### 请求信息

- 请求方法: GET
- 请求路径: `/api/permission/tree`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

无

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Array  | 权限树   |

### 响应示例

```json
{
  "code": 0,
  "message": "获取权限树成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "SYSTEM_MANAGE",
      "type": 1,
      "typeName": "菜单",
      "parentId": 0,
      "path": "/system",
      "icon": "setting",
      "sort": 1,
      "createTime": "2023-01-01 00:00:00",
      "children": [
        {
          "id": 2,
          "name": "用户管理",
          "code": "USER_MANAGE",
          "type": 1,
          "typeName": "菜单",
          "parentId": 1,
          "parentName": "系统管理",
          "path": "/system/user",
          "icon": "user",
          "sort": 1,
          "createTime": "2023-01-01 00:00:00",
          "children": []
        }
      ]
    }
  ]
}
```

## 3. 获取所有权限（平铺结构）

### 接口说明

获取所有权限的平铺结构，需要管理员权限。

### 请求信息

- 请求方法: GET
- 请求路径: `/api/permission/list`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

无

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Array  | 权限列表 |

### 响应示例

```json
{
  "code": 0,
  "message": "获取权限列表成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "SYSTEM_MANAGE",
      "type": 1,
      "typeName": "菜单",
      "parentId": 0,
      "path": "/system",
      "icon": "setting",
      "sort": 1,
      "createTime": "2023-01-01 00:00:00"
    },
    {
      "id": 2,
      "name": "用户管理",
      "code": "USER_MANAGE",
      "type": 1,
      "typeName": "菜单",
      "parentId": 1,
      "parentName": "系统管理",
      "path": "/system/user",
      "icon": "user",
      "sort": 1,
      "createTime": "2023-01-01 00:00:00"
    }
  ]
}
```

## 4. 根据ID获取权限

### 接口说明

根据ID获取权限信息，需要管理员权限。

### 请求信息

- 请求方法: GET
- 请求路径: `/api/permission/{id}`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名 | 类型 | 必填 | 说明    |
|--------|------|------|---------|
| id     | Long | 是   | 权限ID  |

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Object | 权限信息 |

### 响应示例

```json
{
  "code": 0,
  "message": "获取权限成功",
  "data": {
    "id": 1,
    "name": "系统管理",
    "code": "SYSTEM_MANAGE",
    "type": 1,
    "typeName": "菜单",
    "parentId": 0,
    "path": "/system",
    "icon": "setting",
    "sort": 1,
    "createTime": "2023-01-01 00:00:00"
  }
}
```

### 错误码

| 错误码 | 说明         |
|--------|--------------|
| 30003  | 权限不存在   |

## 5. 获取角色的权限列表

### 接口说明

获取指定角色的权限列表，需要管理员权限。

### 请求信息

- 请求方法: GET
- 请求路径: `/api/permission/role/{roleId}`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名 | 类型 | 必填 | 说明    |
|--------|------|------|---------|
| roleId | Long | 是   | 角色ID  |

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Array  | 权限列表 |

### 响应示例

```json
{
  "code": 0,
  "message": "获取角色权限列表成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "SYSTEM_MANAGE",
      "type": 1,
      "typeName": "菜单",
      "parentId": 0,
      "path": "/system",
      "icon": "setting",
      "sort": 1,
      "createTime": "2023-01-01 00:00:00"
    },
    {
      "id": 2,
      "name": "用户管理",
      "code": "USER_MANAGE",
      "type": 1,
      "typeName": "菜单",
      "parentId": 1,
      "parentName": "系统管理",
      "path": "/system/user",
      "icon": "user",
      "sort": 1,
      "createTime": "2023-01-01 00:00:00"
    }
  ]
}
```

## 6. 为角色分配权限

### 接口说明

为指定角色分配权限，需要管理员权限。

### 请求信息

- 请求方法: POST
- 请求路径: `/api/permission/assign`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名       | 类型   | 必填 | 说明                                   |
|--------------|--------|------|----------------------------------------|
| roleId       | Long   | 是   | 角色ID                                 |
| permissionIds| Array  | 是   | 权限ID列表，为空数组表示清空角色的所有权限 |

### 请求示例

```json
{
  "roleId": 1,
  "permissionIds": [1, 2, 3, 4]
}
```

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Boolean| 是否分配成功 |

### 响应示例

```json
{
  "code": 0,
  "message": "分配权限成功",
  "data": true
}
```

### 错误码

| 错误码 | 说明         |
|--------|--------------|
| 20001  | 角色不存在   |
| 30003  | 权限不存在   |

## 7. 删除权限

### 接口说明

删除指定权限（逻辑删除），需要管理员权限。如果权限有子权限，则不允许删除。

### 请求信息

- 请求方法: DELETE
- 请求路径: `/api/permission/{id}`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名 | 类型 | 必填 | 说明    |
|--------|------|------|---------|
| id     | Long | 是   | 权限ID  |

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Boolean| 是否删除成功 |

### 响应示例

```json
{
  "code": 0,
  "message": "删除权限成功",
  "data": true
}
```

### 错误码

| 错误码 | 说明                |
|--------|---------------------|
| 30003  | 权限不存在          |
| 30008  | 该权限有子权限，请先删除子权限 |

## 8. 取消角色的指定权限

### 接口说明

取消角色的指定权限，需要管理员权限。

### 请求信息

- 请求方法: DELETE
- 请求路径: `/api/permission/role/{roleId}/permission/{permissionId}`
- 需要认证: 是
- 需要角色: `ADMIN`

### 请求参数

| 参数名       | 类型 | 必填 | 说明    |
|--------------|------|------|---------|
| roleId       | Long | 是   | 角色ID  |
| permissionId | Long | 是   | 权限ID  |

### 响应参数

| 参数名  | 类型   | 说明     |
|---------|--------|----------|
| code    | Integer| 响应码   |
| message | String | 响应消息 |
| data    | Boolean| 是否取消成功 |

### 响应示例

```json
{
  "code": 0,
  "message": "取消权限成功",
  "data": true
}
```

### 错误码

| 错误码 | 说明         |
|--------|--------------|
| 20001  | 角色不存在   |
| 30003  | 权限不存在   | 