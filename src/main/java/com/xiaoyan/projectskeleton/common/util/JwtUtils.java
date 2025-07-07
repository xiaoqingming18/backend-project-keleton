package com.xiaoyan.projectskeleton.common.util;

import com.xiaoyan.projectskeleton.common.config.JwtConfig;
import com.xiaoyan.projectskeleton.common.exception.BusinessException;
import com.xiaoyan.projectskeleton.common.exception.CommonErrorCode;
import com.xiaoyan.projectskeleton.repository.entity.role.Role;
import com.xiaoyan.projectskeleton.repository.entity.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtConfig jwtConfig;
    private SecretKey secretKey;
    
    /**
     * 初始化密钥
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 生成AccessToken
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param roleCode 角色编码
     * @return AccessToken
     */
    public String generateAccessToken(Long userId, String username, String roleCode) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getAccessTokenExpiration() * 1000);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", roleCode);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer(jwtConfig.getIssuer())
                .signWith(secretKey)
                .compact();
    }
    
    /**
     * 生成RefreshToken
     *
     * @param userId 用户ID
     * @return RefreshToken
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshTokenExpiration() * 1000);
        
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setIssuer(jwtConfig.getIssuer())
                .signWith(secretKey)
                .compact();
    }
    
    /**
     * 从AccessToken中获取用户ID
     *
     * @param token AccessToken
     * @return 用户ID
     */
    public Long getUserIdFromAccessToken(String token) {
        Claims claims = parseAccessToken(token);
        return ((Number) claims.get("userId")).longValue();
    }
    
    /**
     * 从AccessToken中获取用户名
     *
     * @param token AccessToken
     * @return 用户名
     */
    public String getUsernameFromAccessToken(String token) {
        Claims claims = parseAccessToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 从AccessToken中获取角色编码
     *
     * @param token AccessToken
     * @return 角色编码
     */
    public String getRoleFromAccessToken(String token) {
        Claims claims = parseAccessToken(token);
        return claims.get("role", String.class);
    }
    
    /**
     * 从RefreshToken中获取用户ID
     *
     * @param token RefreshToken
     * @return 用户ID
     */
    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = parseRefreshToken(token);
        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * 验证AccessToken是否有效
     *
     * @param token AccessToken
     * @return 是否有效
     */
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("AccessToken验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 验证RefreshToken是否有效
     *
     * @param token RefreshToken
     * @return 是否有效
     */
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("RefreshToken验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据RefreshToken刷新AccessToken
     *
     * @param refreshToken RefreshToken
     * @param user 用户信息
     * @param roleCode 角色编码
     * @return 新的AccessToken
     */
    public String refreshAccessToken(String refreshToken, User user, String roleCode) {
        if (!validateRefreshToken(refreshToken)) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "RefreshToken已过期或无效");
        }
        
        Long userId = getUserIdFromRefreshToken(refreshToken);
        if (!userId.equals(user.getId())) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "RefreshToken与用户不匹配");
        }
        
        return generateAccessToken(user.getId(), user.getUsername(), roleCode);
    }
    
    /**
     * 解析AccessToken
     *
     * @param token AccessToken
     * @return Claims
     */
    private Claims parseAccessToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "AccessToken已过期");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "无效的AccessToken");
        }
    }
    
    /**
     * 解析RefreshToken
     *
     * @param token RefreshToken
     * @return Claims
     */
    private Claims parseRefreshToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "RefreshToken已过期");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED, "无效的RefreshToken");
        }
    }
} 