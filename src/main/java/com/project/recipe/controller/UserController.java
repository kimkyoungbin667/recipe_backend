package com.project.recipe.controller;

import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import com.project.recipe.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/check-id")
    public ResponseEntity<ResponseMessage> checkId(@RequestParam String id) {
        boolean exists = userService.isIdTaken(id);
        if (exists) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "이미 사용 중인 아이디입니다.", null));
        }
        return ResponseEntity.ok(new ResponseMessage(200, "사용 가능한 아이디입니다.", null));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<ResponseMessage> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.isNicknameTaken(nickname);
        if (exists) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "이미 사용 중인 닉네임입니다.", null));
        }
        return ResponseEntity.ok(new ResponseMessage(200, "사용 가능한 닉네임입니다.", null));
    }

    @PostMapping("/update-profile-image")
    public ResponseEntity<ResponseMessage> updateProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            Long userNo = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();

            User user = userRepository.findByUserNo(userNo)
                    .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

            String existingFileName = user.getProfileImage();

            // 파일명을 UUID로 생성하여 중복 방지
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String fileName = userNo + "_" + UUID.randomUUID().toString() + "." + fileExtension;

            // 파일 저장
            userService.updateProfileImage(userNo, file, fileName);

            if (existingFileName != null && !existingFileName.isEmpty()) {
                // 기존 파일 경로 생성
                Path existingFilePath = Paths.get(uploadDir + existingFileName);
                File existingFile = existingFilePath.toFile();

                // 기존 파일이 존재하면 삭제
                if (existingFile.exists()) {
                    boolean deleted = existingFile.delete();
                    if (!deleted) {
                        return ResponseEntity.status(500).body(new ResponseMessage(500, "기존 파일 삭제에 실패했습니다.", null));
                    }
                }
            }

            return ResponseEntity.ok(new ResponseMessage(200, "프로필 이미지가 성공적으로 업데이트되었습니다.", null));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ResponseMessage(500, "파일 저장에 실패했습니다.", null));
        }
    }

    @GetMapping("/profile-image/{fileName}")
    public ResponseEntity<?> getProfileImage(@PathVariable String fileName) {
        try {
            // 파일 경로 설정
            Path filePath = (Path) Paths.get(uploadDir + fileName);
            FileSystemResource resource = new FileSystemResource(filePath.toFile());

            // 파일이 존재하는지 확인
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String fileExtension = getFileExtension(fileName);
            MediaType mediaType = getMediaType(fileExtension);
            // 파일을 응답으로 반환
            return ResponseEntity.ok()
                    .contentType(mediaType) // 이미지 형식에 맞게 설정 (예: JPEG)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot != -1) {
            return fileName.substring(lastIndexOfDot + 1).toLowerCase();
        }
        return "";  // 확장자가 없을 경우 빈 문자열 반환
    }

    private MediaType getMediaType(String fileExtension) {
        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "bmp":
                return MediaType.valueOf("image/bmp");
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // 기본 값 (알 수 없는 형식)
        }
    }
}