package com.xiaoyan.projectskeleton.controller.file;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.file.FileUploadResponseDTO;
import com.xiaoyan.projectskeleton.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    /**
     * 上传单个文件
     *
     * @param file 文件
     * @return 文件上传响应
     */
    @PostMapping("/upload")
    @RequireLogin
    public ApiResponse<FileUploadResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadResponseDTO response = fileService.uploadFile(file);
        return ApiResponse.success(response, "文件上传成功");
    }

    /**
     * 上传单个文件到指定目录
     *
     * @param file 文件
     * @param directory 目录
     * @return 文件上传响应
     */
    @PostMapping("/upload/{directory}")
    @RequireLogin
    public ApiResponse<FileUploadResponseDTO> uploadFileToDirectory(
            @RequestParam("file") MultipartFile file,
            @PathVariable("directory") String directory) {
        FileUploadResponseDTO response = fileService.uploadFile(file, directory);
        return ApiResponse.success(response, "文件上传成功");
    }

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 文件上传响应列表
     */
    @PostMapping("/batch-upload")
    @RequireLogin
    public ApiResponse<List<FileUploadResponseDTO>> batchUploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<FileUploadResponseDTO> responses = fileService.uploadFiles(files);
        return ApiResponse.success(responses, "文件批量上传成功");
    }

    /**
     * 批量上传文件到指定目录
     *
     * @param files 文件列表
     * @param directory 目录
     * @return 文件上传响应列表
     */
    @PostMapping("/batch-upload/{directory}")
    @RequireLogin
    public ApiResponse<List<FileUploadResponseDTO>> batchUploadFilesToDirectory(
            @RequestParam("files") List<MultipartFile> files,
            @PathVariable("directory") String directory) {
        List<FileUploadResponseDTO> responses = fileService.uploadFiles(files, directory);
        return ApiResponse.success(responses, "文件批量上传成功");
    }

    /**
     * 删除文件
     *
     * @param objectName 对象名称
     * @return 操作结果
     */
    @DeleteMapping("/{objectName}")
    @RequireLogin
    public ApiResponse<Void> deleteFile(@PathVariable("objectName") String objectName) {
        fileService.deleteFile(objectName);
        return ApiResponse.success(null, "文件删除成功");
    }
} 