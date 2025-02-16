package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.PostCreateRequest;
import com.project.recipe.dto.PostListResponse;
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
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequest postCreateRequest) {

        // 작성자 번호가 없을 때
        if(postCreateRequest.getAuthorNo() == null) {
            throw new CustomException("작성자 번호가 전달되지 않았습니다.");}
        // 제목이 없을 때
        if(postCreateRequest.getTitle() == null || postCreateRequest.getTitle().isEmpty()) {
            throw new CustomException("제목이 전달되지 않았습니다.");}
        // 내용이 없을 때
        else if (postCreateRequest.getContent() == null || postCreateRequest.getContent().isEmpty()) {
            throw new CustomException("내용이 전달되지 않았습니다.");}
        // 카테고리가 없을 때
        else if (postCreateRequest.getCategory() == null || postCreateRequest.getCategory().isEmpty()) {
            throw new CustomException("카테고리가 전달되지 않았습니다.");}

        Post createdPost = postService.createPost(postCreateRequest);

        return ResponseEntity.ok(new ResponseMessage(200, "게시글이 성공적으로 등록되었습니다.", createdPost));
    }

    // 게시글 전체(목록) 조회
    @GetMapping
    public ResponseEntity<?> getAllPosts(Pageable pageable) {

        Page<PostListResponse> postList = postService.findAllPosts(pageable);

        if(postList.isEmpty()) {
            return ResponseEntity.ok(new ResponseMessage(200, "조회된 게시글이 없습니다.", null));
        }

        return ResponseEntity.ok(new ResponseMessage(200, "게시글 목록이 성공적으로 조회되었습니다.", postList));
    }



}
