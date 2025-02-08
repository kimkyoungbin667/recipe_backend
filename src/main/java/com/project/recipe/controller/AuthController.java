package com.project.recipe.controller;

import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.dto.UserLoginRequest;
import com.project.recipe.service.AuthService;
import com.project.recipe.service.UserService;
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
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest loginRequest) {
        // 로그인 처리 로직 (예: JWT 토큰 발급)
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(token); // 로그인 성공 시 JWT 토큰 반환
    }


}
