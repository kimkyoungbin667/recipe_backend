package com.project.recipe.service;

import com.project.recipe.entity.Post;
import com.project.recipe.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시글 작성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }



}
