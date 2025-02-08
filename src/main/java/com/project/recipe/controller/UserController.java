package com.project.recipe.controller;

import com.project.recipe.dto.UserCreateRequest;
import com.project.recipe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequest requestDTO) {
        userService.createUser(requestDTO);

        return ResponseEntity.ok("회원가입 성공!");
    }
}