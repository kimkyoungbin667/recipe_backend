package com.project.recipe.controller;

import com.project.recipe.entity.AiChat;
import com.project.recipe.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AiChat askQuestion(@RequestParam String message) {

        String aiResponse = aiChatService.getAiResponse(message);

        return aiChatService.saveMessage(message, aiResponse);
    }
}
