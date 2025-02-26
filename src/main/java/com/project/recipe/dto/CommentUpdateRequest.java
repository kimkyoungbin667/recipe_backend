package com.project.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequest { // 댓글 수정 DTO
    private String content;  // 수정할 댓글 내용
}