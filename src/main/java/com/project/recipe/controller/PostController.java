package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.entity.Post;
import com.project.recipe.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> createPost(@RequestBody Post post) {

        // 작성자 번호가 없을 때
        if(post.getUserNo() == null) {
            throw new CustomException("작성자 번호가 없습니다.");}
        // 제목이 없을 때
        if(post.getTitle() == null || post.getTitle().isEmpty()) {
            throw new CustomException("제목이 없습니다.");}
        // 내용이 없을 때
        else if (post.getContent() == null || post.getContent().isEmpty()) {
            throw new CustomException("내용이 없습니다.");}
        // 카테고리가 없을 때
        else if (post.getCategory() == null || post.getCategory().isEmpty()) {
            throw new CustomException("카테고리가 없습니다.");}
        return ResponseEntity.ok(new ResponseMessage(200, "게시글이 성공적으로 등록되었습니다.", postService.createPost(post)));
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(Pageable pageable) {

        return ResponseEntity.ok(postService.findAllPosts(pageable));
    }



}
