package com.project.recipe.service;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.PostCreateRequest;
import com.project.recipe.dto.PostListResponse;
import com.project.recipe.entity.Post;
import com.project.recipe.entity.User;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // 게시글 작성
    @Transactional
    public Post createPost(PostCreateRequest postCreateRequest) {
        // 작성자 ID(authorNo)를 기반으로 User 조회
        User author = userRepository.findById(postCreateRequest.getAuthorNo())
                .orElseThrow(() -> new CustomException("해당 작성자(authorNo: " + postCreateRequest.getAuthorNo() + ")를 찾을 수 없습니다."));

        // Post 객체 생성 및 저장
        Post post = new Post(author, postCreateRequest.getTitle(), postCreateRequest.getContent(), postCreateRequest.getCategory());
        return postRepository.save(post);
    }

    // 게시글 전체 조회
    public Page<PostListResponse> findAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findByIsDeletedFalse(pageable);
        return posts.map(post -> new PostListResponse(
                post.getPostNo(), post.getAuthor().getUserNo(), post.getAuthor().getUserNickname(), post.getAuthor().getUserName(), post.getTitle(), post.getCategory(), post.getCreatedAt()
                ));
    }

    public void saveAllPosts(List<Post> posts) {
        postRepository.saveAll(posts);
    }

}
