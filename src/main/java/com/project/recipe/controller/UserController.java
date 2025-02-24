package com.project.recipe.controller;

import com.project.recipe.dto.ResponseMessage;
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
}