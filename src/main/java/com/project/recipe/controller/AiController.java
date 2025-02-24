package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.entity.AiChat;
import com.project.recipe.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestParam String message) {
        System.out.println("User message: " + message);

        if(message.isEmpty()) {
            throw new CustomException("질문이 전달되지 않았습니다.");
        }

        String aiResponse = aiChatService.getAiResponse(message);
        System.out.println(aiResponse);
        return ResponseEntity.ok(new ResponseMessage(200, "AI 답변이 성공적으로 반환되었습니다.", aiResponse));
    }
}
