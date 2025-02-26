package com.project.recipe.service;

import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Value("${file.upload-dir}") //
    private String uploadDir;

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

    @Transactional
    public void updateProfileImage(Long userNo, MultipartFile file, String fileName) throws IOException {
        // 파일 저장 디렉토리 확인 및 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        Path path = Paths.get(uploadDir + fileName);
        Files.write(path, file.getBytes());  // 파일 저장

        // 사용자 정보 업데이트
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfileImage(fileName); // User 엔티티에 프로필 이미지 경로 저장
        userRepository.save(user);  // 업데이트된 사용자 정보 저장
    }

}