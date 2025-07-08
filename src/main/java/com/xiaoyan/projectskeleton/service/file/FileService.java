package com.xiaoyan.projectskeleton.service.file;

import com.xiaoyan.projectskeleton.repository.dto.file.FileUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件上传响应
     */
    FileUploadResponseDTO uploadFile(MultipartFile file);

    /**
     * 上传文件到指定目录
     *
     * @param file 文件
     * @param directory 目录
     * @return 文件上传响应
     */
    FileUploadResponseDTO uploadFile(MultipartFile file, String directory);
    
    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 文件上传响应列表
     */
    List<FileUploadResponseDTO> uploadFiles(List<MultipartFile> files);
    
    /**
     * 批量上传文件到指定目录
     *
     * @param files 文件列表
     * @param directory 目录
     * @return 文件上传响应列表
     */
    List<FileUploadResponseDTO> uploadFiles(List<MultipartFile> files, String directory);
    
    /**
     * 删除文件
     *
     * @param objectName 对象名称
     */
    void deleteFile(String objectName);
    
    /**
     * 获取文件访问URL
     *
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    String getFileUrl(String objectName);
} 