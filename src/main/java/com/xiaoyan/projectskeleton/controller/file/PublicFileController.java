package com.xiaoyan.projectskeleton.controller.file;

import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.file.FileUploadResponseDTO;
import com.xiaoyan.projectskeleton.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公共文件上传控制器
 * 不需要登录即可访问
 */
@RestController
@RequestMapping("/public/file")
@RequiredArgsConstructor
@Slf4j
public class PublicFileController {

    private final FileService fileService;

    /**
     * 公共文件上传接口
     * 不需要登录即可访问
     *
     * @param file 文件
     * @return 文件上传响应
     */
    @PostMapping("/upload")
    public ApiResponse<FileUploadResponseDTO> uploadPublicFile(@RequestParam("file") MultipartFile file) {
        // 使用public目录存储公共文件
        FileUploadResponseDTO response = fileService.uploadFile(file, "public");
        return ApiResponse.success(response, "文件上传成功");
    }
} 