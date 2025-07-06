package com.xiaoyan.projectskeleton.controller.user;

import com.xiaoyan.projectskeleton.common.util.ApiResponse;
import com.xiaoyan.projectskeleton.repository.dto.RoleDTO;
import com.xiaoyan.projectskeleton.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/user/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public ApiResponse<List<RoleDTO>> listAllRoles() {
        List<RoleDTO> roleList = roleService.listAllRoles();
        return ApiResponse.success(roleList, "获取角色列表成功");
    }
} 