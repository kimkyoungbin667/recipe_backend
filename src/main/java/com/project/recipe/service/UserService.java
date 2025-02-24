package com.project.recipe.service;

import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    // 아이디 중복 검사
    public boolean isIdTaken(String id) {
        return userRepository.existsById(id);
    }

    // 닉네임 중복 검사
    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}