package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.entity.Post;
import com.project.recipe.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostService postService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {

        // 제목이 없다면
        if(post.getTitle() == null || post.getTitle().isEmpty()) {
            throw new CustomException("제목을 입력해주세요.");}
        // 내용이 없다면
        else if (post.getContent() == null || post.getContent().isEmpty()) {
            throw new CustomException("내용을 입력해주세요.");}

        return ResponseEntity.ok(postService.createPost(post));
    }


}
