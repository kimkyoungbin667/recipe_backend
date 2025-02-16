package com.project.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCreateRequest { // 게시글 작성 DTO

    private Long authorNo;        // 작성자 번호
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private String category;    // 카테고리

}
