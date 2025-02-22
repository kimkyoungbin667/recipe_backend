package com.project.recipe.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse { // 게시글 전체(목록) 조회 DTO

    private Long postNo;              // 게시글 번호
    private Long authorNo;            // 작성자 번호
    private String authorNickname;    // 작성자 닉네임
    private String authorName;        // 작성자 이름
    private String title;             // 게시글 제목
    private String category;          // 카테고리
    private String createdAt;  // 게시글 작성일

}
