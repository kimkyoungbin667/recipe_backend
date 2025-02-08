package com.project.recipe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationFilter(@Lazy JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        // 토큰이 존재하고 유효한 경우 인증 처리
        if (token != null && jwtTokenUtil.validateToken(token, jwtTokenUtil.getUsernameFromToken(token))) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    jwtTokenUtil.getUsernameFromToken(token), null, null  // 사용자명, 패스워드는 없으므로 null
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증 정보를 SecurityContext에 설정
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    // 요청에서 JWT 토큰 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // "Bearer "를 제거한 토큰 반환
        }
        return null;
    }
}
