package com.xiaoyan.projectskeleton.service.impl.file;

import com.xiaoyan.projectskeleton.common.config.MinioConfig;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.FileErrorCode;
import com.xiaoyan.projectskeleton.repository.dto.file.FileUploadResponseDTO;
import com.xiaoyan.projectskeleton.service.file.FileService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public FileUploadResponseDTO uploadFile(MultipartFile file) {
        return uploadFile(file, null);
    }

    @Override
    public FileUploadResponseDTO uploadFile(MultipartFile file, String directory) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new BusinessException(FileErrorCode.FILE_EMPTY);
            }

            // 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.substringAfterLast(originalFilename, ".");
            
            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
            
            // 构建对象名称（存储路径）
            String objectName;
            if (StringUtils.isNotBlank(directory)) {
                // 使用指定目录
                objectName = directory + "/" + fileName;
            } else {
                // 使用日期作为默认目录
                String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                objectName = dateDir + "/" + fileName;
            }
            
            // 上传文件到MinIO
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            
            minioClient.putObject(putObjectArgs);
            
            // 构建文件访问URL
            String url = getFileUrl(objectName);
            
            // 返回上传结果
            return FileUploadResponseDTO.builder()
                    .fileName(originalFilename)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .url(url)
                    .objectName(objectName)
                    .build();
            
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new BusinessException(FileErrorCode.FILE_UPLOAD_FAILED, "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public List<FileUploadResponseDTO> uploadFiles(List<MultipartFile> files) {
        return uploadFiles(files, null);
    }

    @Override
    public List<FileUploadResponseDTO> uploadFiles(List<MultipartFile> files, String directory) {
        List<FileUploadResponseDTO> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(uploadFile(file, directory));
        }
        return results;
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            // 检查文件是否存在
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
            
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            throw new BusinessException(FileErrorCode.FILE_DELETE_FAILED, "文件删除失败: " + e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        try {
            // 使用公共访问URL
            return minioConfig.getPublicUrl() + "/" + minioConfig.getBucketName() + "/" + objectName;
        } catch (Exception e) {
            log.error("获取文件URL失败: {}", e.getMessage(), e);
            throw new BusinessException(FileErrorCode.FILE_NOT_EXIST, "获取文件URL失败: " + e.getMessage());
        }
    }
} 