package com.xiaoyan.projectskeleton.controller.role;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.annotation.RequireRoles;
import com.xiaoyan.projectskeleton.common.enums.RoleEnum;
import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.role.RoleDTO;
import com.xiaoyan.projectskeleton.repository.dto.role.RoleCreateDTO;
import com.xiaoyan.projectskeleton.service.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    @RequireLogin
    public ApiResponse<List<RoleDTO>> listAllRoles() {
        log.info("获取所有角色列表");
        List<RoleDTO> roleList = roleService.listAllRoles();
        return ApiResponse.success(roleList, "获取角色列表成功");
    }
    
    /**
     * 创建角色
     *
     * @param createDTO 角色创建DTO
     * @return 创建后的角色
     */
    @PostMapping("/create")
    @RequireLogin
    @RequireRoles(RoleEnum.ADMIN)
    public ApiResponse<RoleDTO> createRole(@RequestBody @Validated RoleCreateDTO createDTO) {
        log.info("创建角色: {}", createDTO.getName());
        RoleDTO roleDTO = roleService.createRole(createDTO);
        return ApiResponse.success(roleDTO, "创建角色成功");
    }
    
    /**
     * 检查角色编码是否已存在
     *
     * @param code 角色编码
     * @return 是否存在
     */
    @GetMapping("/check-code")
    @RequireLogin
    public ApiResponse<Boolean> checkRoleCode(@RequestParam String code) {
        boolean exists = roleService.checkCodeExists(code);
        return ApiResponse.success(exists);
    }
} 