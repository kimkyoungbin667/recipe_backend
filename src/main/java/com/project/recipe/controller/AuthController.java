package com.project.recipe.controller;

import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.dto.UserLoginRequest;
import com.project.recipe.service.AuthService;
import com.project.recipe.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(UserService userService, AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> loginUser(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        // 로그인 처리 로직 (예: JWT 토큰 발급)
        String token = authService.login(loginRequest);

    // JWT 토큰을 HTTP-only 쿠키로 설정
        Cookie cookie = new Cookie("jwt", token);  // 쿠키 이름은 "jwt", 값은 발급된 JWT 토큰
        cookie.setHttpOnly(false);  // 클라이언트에서 JavaScript로 접근할 수 없도록 설정
        cookie.setSecure(false);    // HTTPS에서만 쿠키가 전송되도록 설정
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

}
