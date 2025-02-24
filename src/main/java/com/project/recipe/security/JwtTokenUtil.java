package com.project.recipe.security;

import com.project.recipe.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration_time}")
    private long expirationTime;

    // JWT 토큰 생성
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId()) // 주체로 username 설정
                .claim("nickname", user.getNickname()) // 사용자 ID를 클레임으로 추가
                .claim("roles", user.getRole()) // 사용자 역할을 클레임으로 추가 (예: "ADMIN", "USER")
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 비밀 키로 서명
                .compact();
    }

    // JWT 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰이 만료되었는지 확인
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // 토큰의 만료 날짜 추출
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token, String username) {
        return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }
}