package com.gov.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 提供 Token 生成、解析、校验等功能
 */
public class JwtUtil {

    /** 默认密钥（生产环境必须通过 Nacos 配置注入） */
    private static final String DEFAULT_SECRET = "GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation";

    /** 运行时可配置的密钥（由 JwtConfig 初始化） */
    private static String SECRET = DEFAULT_SECRET;

    /** Token 默认有效期（毫秒）：2 小时 */
    public static final long DEFAULT_EXPIRE_TIME = 2 * 60 * 60 * 1000L;

    /** Token 默认刷新时间（毫秒）：30 分钟 */
    public static final long DEFAULT_REFRESH_TIME = 30 * 60 * 1000L;

    /** 签发者 */
    private static final String ISSUER = "gov-platform";

    /**
     * 设置 JWT 密钥（由配置中心注入，支持密钥轮换）
     *
     * @param secret JWT 签名密钥
     */
    public static void setSecret(String secret) {
        if (secret != null && !secret.isEmpty()) {
            SECRET = secret;
        }
    }

    /**
     * 生成 JWT Token（默认有效期 2 小时）
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, null, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 生成 JWT Token（自定义有效期）
     *
     * @param userId     用户ID
     * @param username   用户名
     * @param claims     额外声明
     * @param expireTime 有效期（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String username, Map<String, Object> claims, long expireTime) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        JwtBuilder builder = Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("username", username)
            .issuer(ISSUER)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(key);

        // 添加自定义声明
        if (claims != null && !claims.isEmpty()) {
            claims.forEach(builder::claim);
        }

        return builder.compact();
    }

    /**
     * 生成 JWT Token（携带角色信息）
     *
     * @param userId     用户ID
     * @param username   用户名
     * @param roles      角色列表（逗号分隔）
     * @param deptId     部门ID
     * @param expireTime 有效期（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateTokenWithRoles(Long userId, String username, String roles, Long deptId, long expireTime) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("username", username)
            .claim("roles", roles)
            .claim("deptId", deptId)
            .issuer(ISSUER)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expireTime))
            .signWith(key)
            .compact();
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT Token
     * @return Claims 对象
     * @throws JwtException Token 无效或过期
     */
    public static Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public static Long getUserId(String token) {
        try {
            Claims claims = parseToken(token);
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取用户名
     */
    public static String getUsername(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取角色列表
     */
    public static String getRoles(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.get("roles", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取部门ID
     */
    public static Long getDeptId(String token) {
        try {
            Claims claims = parseToken(token);
            Object deptId = claims.get("deptId");
            if (deptId instanceof Integer) {
                return ((Integer) deptId).longValue();
            }
            return (Long) deptId;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验 Token 是否有效
     */
    public static boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 判断 Token 是否即将过期（剩余时间小于 refreshTime）
     *
     * @param token       JWT Token
     * @param refreshTime 刷新阈值（毫秒）
     * @return true 需要刷新
     */
    public static boolean needRefresh(String token, long refreshTime) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            return expiration.getTime() - System.currentTimeMillis() < refreshTime;
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 刷新 Token（保持原有内容，仅延长过期时间）
     *
     * @param token 原 Token
     * @return 新 Token
     */
    public static String refreshToken(String token) {
        try {
            Claims claims = parseToken(token);
            Long userId = Long.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            return generateToken(userId, username);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 Token 剩余有效时间（毫秒）
     */
    public static long getRemainingTime(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().getTime() - System.currentTimeMillis();
        } catch (JwtException e) {
            return 0;
        }
    }
}
