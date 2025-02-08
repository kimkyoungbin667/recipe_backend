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
    private final PasswordEncoder passwordEncoder;  // PasswordEncoder로 변경

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserCreateRequest requestDTO) {
        // 아이디 중복 검사
        if (userRepository.findById(requestDTO.getId()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());

        // User 객체 생성 후 저장
        User user = new User();
        user.setId(requestDTO.getId());
        user.setPassword(encodedPassword);
        user.setEmail(requestDTO.getEmail());
        user.setName(requestDTO.getName());
        user.setNickname(requestDTO.getNickname());
        user.setEmailVerified(false);
        user.setActive(true);

        return userRepository.save(user);
    }


}