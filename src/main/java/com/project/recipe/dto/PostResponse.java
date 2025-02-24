package com.project.recipe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.recipe.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PostResponse {
    private Long postNo;
    private String title;
    private String content;
    private String category;
    private String createdAt;
    private String updatedAt;
    private AuthorResponse author;
    private List<CommentResponse> comments;

    public PostResponse(Post post, List<CommentResponse> comments) {
        this.postNo = post.getPostNo();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.author = new AuthorResponse(post.getAuthor());
        this.comments = comments;
    }
}