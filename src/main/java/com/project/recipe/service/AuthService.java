package com.project.recipe.service;

import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.dto.UserLoginRequest;
import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import com.project.recipe.security.JwtTokenUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;  // UserRepository 의존성 주입
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;  // JWT 토큰 생성 클래스
    private final JavaMailSenderImpl mailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, JavaMailSenderImpl mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.mailSender = mailSender;
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
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

        //닉네임 중복 검사
        if (userRepository.findByNickname(requestDTO.getNickname()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
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

    public String findUserIdByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);  // 이메일로 사용자를 조회
        if (user != null) {
            return user.get().getId();  // 사용자가 존재하면 아이디 반환
        }
        return null;  // 사용자가 없으면 null 반환
    }


    public String resetPassword(String email, String id) {
        User user = userRepository.findByEmailAndId(email, id);  // 이메일과 아이디로 사용자를 조회
        if (user == null) {
            return null;  // 사용자가 없으면 null 반환
        }

        String tempPassword = generateTemporaryPassword(); // 임시 비밀번호 생성

        // 임시 비밀번호로 비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(tempPassword));  // 비밀번호는 암호화하여 저장
        userRepository.save(user);

        // 이메일로 임시 비밀번호 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 초기화");
        message.setText("회원님의 임시 비밀번호는: " + tempPassword + "입니다. 로그인 후 비밀번호를 변경해주세요.");
        mailSender.send(message);

        return tempPassword;  // 임시 비밀번호 반환
    }

    private String generateTemporaryPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder tempPassword = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            tempPassword.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return tempPassword.toString();
    }

}