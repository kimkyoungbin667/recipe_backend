package com.project.recipe.controller;

import com.project.recipe.dto.*;
import com.project.recipe.entity.User;
import com.project.recipe.service.AuthService;
import com.project.recipe.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JavaMailSenderImpl mailSender;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public AuthController(UserService userService, AuthService authService, JavaMailSenderImpl mailSender) {
        this.authService = authService;
        this.mailSender = mailSender;
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseMessage> createUser(@Valid @RequestBody UserCreateRequest requestDTO) {
        authService.createUser(requestDTO);

        return ResponseEntity.ok(new ResponseMessage(200,"회원가입 성공",null));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> loginUser(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        // 로그인 처리 로직 (예: JWT 토큰 발급)
        String token = authService.login(loginRequest);

    // JWT 토큰을 HTTP-only 쿠키로 설정
        Cookie cookie = new Cookie("jwt", token);  // 쿠키 이름은 "jwt", 값은 발급된 JWT 토큰
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");       // 도메인 전체에서 유효
        cookie.setMaxAge(3600);    // 쿠키 만료 시간 (1시간)
        response.addCookie(cookie);  // 응답에 쿠키 추가

        return ResponseEntity.ok(new ResponseMessage(200,"로그인되었습니다.",token)); // 로그인 성공 시 JWT 토큰 반환
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료

        response.addCookie(cookie);

        return ResponseEntity.ok(new ResponseMessage(200, "로그아웃되었습니다.", null));
    }

    @PostMapping("/send-verify-code")
    public ResponseEntity<ResponseMessage> sendEmail(@RequestBody EmailRequest request) {
        String email = request.getEmail();

        if (authService.isEmailRegistered(email)) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "이미 가입된 이메일입니다.", null));
        }

        String code = generateVerificationCode();

        // 이메일 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 이메일 인증 코드");
        message.setText("인증 코드: " + code);
        mailSender.send(message);

        // 서버 메모리에 인증 코드 저장
        verificationCodes.put(email, code);

        // 5분 후 자동 삭제
        scheduler.schedule(() -> verificationCodes.remove(email), 5, TimeUnit.MINUTES);

        return ResponseEntity.ok( new ResponseMessage(200,"인증 코드가 이메일로 전송되었습니다.",null));
    }

    @PostMapping("/verify-email-code")
    public ResponseEntity<ResponseMessage> verifyEmail(@RequestBody EmailVerificationRequest request) {
        String storedCode = verificationCodes.get(request.getEmail());

        if (storedCode != null && storedCode.equals(request.getCode())) {
            verificationCodes.remove(request.getEmail()); // 인증 완료 후 삭제
            return ResponseEntity.ok(new ResponseMessage(200, "이메일 인증 성공",null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "인증 코드가 일치하지 않습니다.", null));
        }
    }

    // 🔹 랜덤 6자리 인증 코드 생성 메서드
    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 100000 ~ 999999
    }

    @PostMapping("/find-id")
    public ResponseEntity<ResponseMessage> findUserId(@RequestBody EmailRequest request) {
        String email = request.getEmail();
        String userId = authService.findUserIdByEmail(email); // 이메일로 사용자 아이디 찾기

        if (userId == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "등록된 이메일이 없습니다.", null));
        }

        // 아이디 찾기 성공 시 이메일로 아이디 전송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("아이디 찾기");
        message.setText("회원님의 아이디는: " + userId );
        mailSender.send(message);

        return ResponseEntity.ok(new ResponseMessage(200, "아이디가 이메일로 전송되었습니다.", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody PasswordResetRequest request) {
        String email = request.getEmail();
        String userId = request.getId();
        String tempPassword = authService.resetPassword(email, userId); // 임시 비밀번호 발급

        if (tempPassword == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage(400, "등록된 이메일 또는 아이디가 없습니다.", null));
        }

        return ResponseEntity.ok(new ResponseMessage(200, "임시 비밀번호가 이메일로 전송되었습니다.", null));
    }
}
