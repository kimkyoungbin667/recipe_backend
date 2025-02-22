package com.project.recipe.service;

import com.project.recipe.entity.AiChat;
import com.project.recipe.repository.AiChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiChatService {

    private final AiChatRepository aiChatRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public AiChatService(AiChatRepository aiChatRepository) {
        this.aiChatRepository = aiChatRepository;
    }

    public String getAiResponse(String userMessage) {
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // OpenAI API 요청 데이터 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", new Object[]{
                Map.of("role", "user", "content", userMessage)
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody != null && responseBody.containsKey("choices")) {
            Map<String, Object> choice = (Map<String, Object>) ((java.util.List<?>) responseBody.get("choices")).get(0);
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            return (String) message.get("content");
        }

        return "AI 응답을 가져오지 못했습니다.";
    }

    public AiChat saveMessage(String userMessage, String aiResponse) {
        AiChat message = new AiChat();
        message.setUserMessage(userMessage);
        message.setAiResponse(aiResponse);
        return aiChatRepository.save(message);
    }
}
