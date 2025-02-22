package com.project.recipe.dto;

import com.project.recipe.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentNo;
    private String content;
    private String createdAt;
    private AuthorResponse author;
    private List<CommentResponse> childComments;

    public CommentResponse(Comment comment, List<CommentResponse> childComments) {
        this.commentNo = comment.getCommentNo();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt(); // String 그대로 사용
        this.author = new AuthorResponse(comment.getAuthor());
        this.childComments = childComments;
    }
}