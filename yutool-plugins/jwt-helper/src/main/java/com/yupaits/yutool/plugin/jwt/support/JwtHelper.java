package com.yupaits.yutool.plugin.jwt.support;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * JWT辅助工具类
 * @author yupaits
 * @date 2019/8/21
 */
@Slf4j
public class JwtHelper {
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProps jwtProps;

    public JwtHelper(JwtProps jwtProps) {
        this.jwtProps = jwtProps;
    }

    /**
     * 从Request中获取Token
     * @param request 请求体
     * @return Token
     */
    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtProps.getAuthHeader());
        if (StringUtils.startsWithIgnoreCase(authHeader, TOKEN_PREFIX)) {
            return StringUtils.substringAfter(authHeader, TOKEN_PREFIX);
        } else {
            throw new JwtException("token不存在或格式有误");
        }
    }

    /**
     * 从Token中获取Claims
     * @param token Token
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProps.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            log.error("token解析错误", e);
            throw new JwtException("token解析错误", e);
        }
    }

    /**
     * 获取Token的过期时间
     * @param token Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 从Token中获取Subject
     * @param token Token
     * @return Subject
     */
    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 为指定用户生成Token
     * @param claims 用户信息
     * @return Token
     */
    public String generateToken(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = getExpirationTime();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdTime)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, jwtProps.getSecret())
                .compact();
    }

    /**
     * 判断Token是否合法
     * @param token Token
     * @return Token未过期返回true，否则返回false
     */
    public boolean isTokenValid(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return !expiration.before(new Date());
    }

    /**
     * 计算Token的过期时间
     * @return 过期时间
     */
    private Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + jwtProps.getExpiredIn() * 1000);
    }
}
