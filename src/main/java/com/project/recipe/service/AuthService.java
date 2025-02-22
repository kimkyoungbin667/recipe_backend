package com.project.recipe.service;

import com.project.recipe.dto.UserLoginRequest;
import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import com.project.recipe.security.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;  // UserRepository 의존성 주입
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;  // JWT 토큰 생성 클래스

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public String login(UserLoginRequest loginRequest) {
        // 사용자명으로 사용자 검색
        User user = userRepository.findById(loginRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        return jwtTokenUtil.generateToken(user);
    }
}