package com.project.recipe.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PostUpdateRequest {
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private String category;    // 게시글 카테고리
}