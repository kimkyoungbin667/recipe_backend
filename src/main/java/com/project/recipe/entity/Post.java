package com.project.recipe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="tb_post")
@Setter
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // PK 자동 증가
    private Long postNo;  // 게시글 번호

    @Column(nullable = false)
    private Long userNo;  // 작성자 번호

    @Column(nullable = false)
    private String category; // 카테고리

    @Column(nullable = false)
    private String title;  // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 게시글 내용

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now().withNano(0); // 게시글 작성일 (마이크로초는 0으로)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now().withNano(0); // 게시글 수정일 (마이크로초는 0으로)

    private Boolean isDeleted = false;  // 게시글 삭제 여부

}
