package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.CommentCreateRequest;
import com.project.recipe.dto.CommentResponse;
import com.project.recipe.dto.CommentUpdateRequest;
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

    // 댓글(대댓글) 조회
    @GetMapping("/{commentNo}")
    public ResponseEntity<?> getComment(@PathVariable Long commentNo) {

        if (commentNo < 0) {
            throw new CustomException("올바른 댓글(대댓글) 번호를 입력해주세요.");
        }

        CommentResponse commentResponse = commentService.getCommentById(commentNo);
        return ResponseEntity.ok(new ResponseMessage(200, "댓글(대댓글) 조회 성공", commentResponse));
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

    // 댓글(대댓글) 수정
    @PutMapping("/{commentNo}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentNo, @RequestBody CommentUpdateRequest request) {

        if (commentNo < 0) {
            throw new CustomException("올바른 댓글(대댓글) 번호를 입력해주세요.");
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new CustomException("댓글(대댓글) 수정 내용이 전달되지 않았습니다.");
        }

        CommentResponse updatedComment = commentService.updateComment(commentNo, request);
        return ResponseEntity.ok(new ResponseMessage(200, "댓글(대댓글)이 성공적으로 수정되었습니다.", updatedComment));
    }

    // 댓글(대댓글) 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentNo) {
        commentService.deleteComment(commentNo);
        return ResponseEntity.ok(new ResponseMessage(200, "댓글(대댓글)이 성공적으로 삭제되었습니다.", null));
    }
}