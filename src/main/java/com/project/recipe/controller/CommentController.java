package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.CommentCreateRequest;
import com.project.recipe.dto.CommentResponse;
import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.entity.Comment;
import com.project.recipe.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성 (일반 댓글 및 대댓글)
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentCreateRequest request) {

        if (request.getPostNo() < 0) {
            throw new CustomException("올바른 게시글 번호를 입력해주세요.");
        }
        if (request.getAuthorNo() == null) {
            throw new CustomException("댓글(대댓글) 작성자가 전달되지 않았습니다.");
        }
        if (request.getContent() == null) {
            throw new CustomException("댓글(대댓글) 내용이 전달되지 않았습니다.");
        }

        CommentResponse commentResponse = commentService.createComment(request);
        return ResponseEntity.ok(new ResponseMessage(200, "댓글(대댓글)이 성공적으로 추가되었습니다.", commentResponse));
    }

    // 댓글(대댓글) 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentNo) {
        commentService.deleteComment(commentNo);
        return ResponseEntity.ok(new ResponseMessage(200, "댓글(대댓글)이 성공적으로 삭제되었습니다.", null));
    }
}