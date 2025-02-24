package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name= "tb_aichat")
public class AiChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_no;

    @Column(columnDefinition = "TEXT")
    private String userMessage;  // 사용자가 입력한 질문

    @Column(columnDefinition = "LONGTEXT")
    private String aiResponse;   // AI가 생성한 답변 (4GB 저장 가능)

    private LocalDateTime timestamp = LocalDateTime.now();
}