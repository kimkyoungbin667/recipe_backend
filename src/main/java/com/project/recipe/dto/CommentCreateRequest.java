package com.project.recipe.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CommentCreateRequest { // 댓글 및 대댓글 작성 DTO

    private Long postNo;        // 게시글 번호
    private Long authorNo;      // 댓글 쓴 사람
    private String content;     // 댓글 내용
    private Long parentCommentNo; // 댓글인지 대댓글인지 (이 값이 존재한다면 대댓글, 없으면 댓글)

}
