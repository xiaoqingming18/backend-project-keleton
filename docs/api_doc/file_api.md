# 文件上传API文档

## 1. 文件上传接口

### 1.1 上传单个文件

- **URL**: `/api/file/upload`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **需要认证**: 是
- **请求参数**:

| 参数名 | 类型 | 是否必须 | 描述 |
| ------ | ---- | -------- | ---- |
| file   | File | 是       | 文件 |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件上传成功",
  "data": {
    "fileName": "example.jpg",
    "fileSize": 12345,
    "contentType": "image/jpeg",
    "url": "http://localhost:9000/files/2023/05/20/uuid.jpg",
    "objectName": "2023/05/20/uuid.jpg"
  }
}
```

### 1.2 上传单个文件到指定目录

- **URL**: `/api/file/upload/{directory}`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **需要认证**: 是
- **请求参数**:

| 参数名    | 类型   | 是否必须 | 描述     |
| --------- | ------ | -------- | -------- |
| file      | File   | 是       | 文件     |
| directory | String | 是       | 目录名称 |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件上传成功",
  "data": {
    "fileName": "example.jpg",
    "fileSize": 12345,
    "contentType": "image/jpeg",
    "url": "http://localhost:9000/files/images/uuid.jpg",
    "objectName": "images/uuid.jpg"
  }
}
```

### 1.3 批量上传文件

- **URL**: `/api/file/batch-upload`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **需要认证**: 是
- **请求参数**:

| 参数名 | 类型       | 是否必须 | 描述     |
| ------ | ---------- | -------- | -------- |
| files  | File Array | 是       | 文件列表 |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件批量上传成功",
  "data": [
    {
      "fileName": "example1.jpg",
      "fileSize": 12345,
      "contentType": "image/jpeg",
      "url": "http://localhost:9000/files/2023/05/20/uuid1.jpg",
      "objectName": "2023/05/20/uuid1.jpg"
    },
    {
      "fileName": "example2.jpg",
      "fileSize": 23456,
      "contentType": "image/jpeg",
      "url": "http://localhost:9000/files/2023/05/20/uuid2.jpg",
      "objectName": "2023/05/20/uuid2.jpg"
    }
  ]
}
```

### 1.4 批量上传文件到指定目录

- **URL**: `/api/file/batch-upload/{directory}`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **需要认证**: 是
- **请求参数**:

| 参数名    | 类型       | 是否必须 | 描述     |
| --------- | ---------- | -------- | -------- |
| files     | File Array | 是       | 文件列表 |
| directory | String     | 是       | 目录名称 |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件批量上传成功",
  "data": [
    {
      "fileName": "example1.jpg",
      "fileSize": 12345,
      "contentType": "image/jpeg",
      "url": "http://localhost:9000/files/images/uuid1.jpg",
      "objectName": "images/uuid1.jpg"
    },
    {
      "fileName": "example2.jpg",
      "fileSize": 23456,
      "contentType": "image/jpeg",
      "url": "http://localhost:9000/files/images/uuid2.jpg",
      "objectName": "images/uuid2.jpg"
    }
  ]
}
```

### 1.5 删除文件

- **URL**: `/api/file/{objectName}`
- **Method**: `DELETE`
- **需要认证**: 是
- **请求参数**:

| 参数名     | 类型   | 是否必须 | 描述                       |
| ---------- | ------ | -------- | -------------------------- |
| objectName | String | 是       | 对象名称（文件存储路径）   |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件删除成功",
  "data": null
}
```

## 2. 公共文件上传接口

### 2.1 上传公共文件

- **URL**: `/api/public/file/upload`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **需要认证**: 否
- **请求参数**:

| 参数名 | 类型 | 是否必须 | 描述 |
| ------ | ---- | -------- | ---- |
| file   | File | 是       | 文件 |

- **响应结果**:

```json
{
  "code": 200,
  "success": true,
  "message": "文件上传成功",
  "data": {
    "fileName": "example.jpg",
    "fileSize": 12345,
    "contentType": "image/jpeg",
    "url": "http://localhost:9000/files/public/uuid.jpg",
    "objectName": "public/uuid.jpg"
  }
}
```

## 3. 错误码

| 错误码 | 描述             |
| ------ | ---------------- |
| 50001  | 文件不能为空     |
| 50002  | 文件类型不支持   |
| 50003  | 文件大小超过限制 |
| 50004  | 文件上传失败     |
| 50005  | 文件不存在       |
| 50006  | 文件删除失败     | 