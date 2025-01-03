package com.example.todolist_advanced.common.utils;

import com.example.todolist_advanced.common.enums.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String BARER_PREFIX = "Bearer ";
    private final long ACCESS_TOKEN_TIME = 60*30*1000L;
    private final long REFRESH_TOKEN_TIME = 60*60*1000L;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //Base64로 인코딩된 문자열 secretKey를 디코딩하여 바이트배열로 반환
        key = Keys.hmacShaKeyFor(bytes); //디코딩된 바이트 배열을 해시 기반 메시지 인증 코드(HMAC)알고리즘에 사용할 키 객체를 생성함
    }

    public String extractUsername(String token) { return extractAllClaims(token).getSubject(); }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(Long userId, String username, UserRoleEnum userRole) {
        Date date = new Date();
        return BARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim("auth",userRole)
                        .claim("userId", userId)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public String generateRefreshToken(Long userId, String username, UserRoleEnum userRole) {
        Date date = new Date();
        return BARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim("auth",userRole)
                        .claim("userId", userId)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature.", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty.", e);
        }
        return false;
    }

    public String extractRoles(String token) { return Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody()
            .get("auth", String.class);}


    public Long extractUserId(String jwt) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody()
                .get("userId",Long.class);
    }

    public boolean hasRole(String token, String role) {
        String roles = extractRoles(token);
        return roles.contains(role);
    }
}
