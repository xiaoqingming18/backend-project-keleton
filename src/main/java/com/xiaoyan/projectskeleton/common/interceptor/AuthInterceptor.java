package com.xiaoyan.projectskeleton.common.interceptor;

import com.xiaoyan.projectskeleton.common.annotation.RequireLogin;
import com.xiaoyan.projectskeleton.common.annotation.RequireRoles;
import com.xiaoyan.projectskeleton.common.context.UserContext;
import com.xiaoyan.projectskeleton.common.enums.RoleEnum;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.CommonErrorCode;
import com.xiaoyan.projectskeleton.common.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 认证拦截器
 * 用于处理Token和注解
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    
    /**
     * 请求头中的Token字段名
     */
    private static final String TOKEN_HEADER = "Authorization";
    
    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是请求方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        // 尝试获取Token
        String token = getTokenFromRequest(request);
        
        // 检查方法上是否有@RequireLogin注解
        RequireLogin methodLogin = method.getAnnotation(RequireLogin.class);
        // 检查类上是否有@RequireLogin注解
        RequireLogin classLogin = handlerMethod.getBeanType().getAnnotation(RequireLogin.class);
        
        // 判断是否需要登录
        boolean requireLogin = (methodLogin != null && methodLogin.value()) || 
                              (classLogin != null && classLogin.value() && methodLogin == null);
        
        if (requireLogin) {
            // 需要登录但没有Token
            if (!StringUtils.hasText(token)) {
                throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "请先登录");
            }
            
            // 验证Token
            if (!jwtUtils.validateAccessToken(token)) {
                throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "Token已过期或无效");
            }
            
            // 解析Token，获取用户信息
            Long userId = jwtUtils.getUserIdFromAccessToken(token);
            String username = jwtUtils.getUsernameFromAccessToken(token);
            String roleCode = jwtUtils.getRoleFromAccessToken(token);
            
            // 设置用户上下文
            UserContext userContext = new UserContext()
                    .setUserId(userId)
                    .setUsername(username)
                    .setRoleCode(roleCode);
            UserContext.setCurrentUser(userContext);
            
            // 检查方法上是否有@RequireRoles注解
            RequireRoles methodRoles = method.getAnnotation(RequireRoles.class);
            // 检查类上是否有@RequireRoles注解
            RequireRoles classRoles = handlerMethod.getBeanType().getAnnotation(RequireRoles.class);
            
            // 获取角色要求
            RequireRoles requireRoles = methodRoles != null ? methodRoles : classRoles;
            
            // 如果有角色要求，则验证角色
            if (requireRoles != null && requireRoles.value().length > 0) {
                boolean hasRole = false;
                
                if (requireRoles.logical() == RequireRoles.Logical.OR) {
                    // 满足任一角色即可
                    hasRole = Arrays.stream(requireRoles.value())
                            .anyMatch(role -> role.getCode().equals(roleCode));
                } else {
                    // 必须满足所有角色（实际上多角色AND逻辑很少用到）
                    hasRole = Arrays.stream(requireRoles.value())
                            .allMatch(role -> role.getCode().equals(roleCode));
                }
                
                if (!hasRole) {
                    throw new BusinessException(CommonErrorCode.FORBIDDEN, "权限不足");
                }
            }
        } else {
            // 不需要登录，但如果有Token，也尝试解析
            if (StringUtils.hasText(token) && jwtUtils.validateAccessToken(token)) {
                Long userId = jwtUtils.getUserIdFromAccessToken(token);
                String username = jwtUtils.getUsernameFromAccessToken(token);
                String roleCode = jwtUtils.getRoleFromAccessToken(token);
                
                // 设置用户上下文
                UserContext userContext = new UserContext()
                        .setUserId(userId)
                        .setUsername(username)
                        .setRoleCode(roleCode);
                UserContext.setCurrentUser(userContext);
            }
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除用户上下文
        UserContext.clear();
    }
    
    /**
     * 从请求头中获取Token
     * 
     * @param request HTTP请求
     * @return Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
} 