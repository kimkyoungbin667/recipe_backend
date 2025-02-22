package com.project.recipe.service;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.*;
import com.project.recipe.entity.Comment;
import com.project.recipe.entity.Post;
import com.project.recipe.entity.User;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 게시글 작성
    @Transactional
    public Post createPost(PostCreateRequest postCreateRequest) {
        User author = userRepository.findById(postCreateRequest.getAuthorNo())
                .orElseThrow(() -> new CustomException("해당 작성자를 찾을 수 없습니다."));

        Post post = new Post(author, postCreateRequest.getTitle(), postCreateRequest.getContent(), postCreateRequest.getCategory());

        // 현재 시간 저장
        String now = LocalDateTime.now().format(FORMATTER);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        return postRepository.save(post);
    }

    // 게시글 전체 조회
    public Page<PostListResponse> findAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findByIsDeletedFalse(pageable);
        return posts.map(post -> new PostListResponse(
                post.getPostNo(), post.getAuthor().getUserNo(), post.getAuthor().getNickname(), post.getAuthor().getName(), post.getTitle(), post.getCategory(), post.getCreatedAt()
                ));
    }

    // 게시글 상세 조회 (댓글 포함)
    @Transactional
    public PostResponse getPostWithComments(Long postNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new CustomException("해당 게시글을 찾을 수 없습니다."));

        Hibernate.initialize(post.getComments());

        // ✅ 삭제되지 않은 부모 댓글만 가져오고, 최신순 정렬
        List<CommentResponse> commentResponses = post.getComments().stream()
                .filter(comment -> !comment.getIsDeleted()) //  삭제되지 않은 댓글만 조회
                .filter(comment -> comment.getParent() == null) //  최상위 댓글만 필터링
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());

        return new PostResponse(post, commentResponses);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        List<CommentResponse> childCommentResponses = comment.getChildComments().stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());

        return new CommentResponse(comment, childCommentResponses);
    }

    // 게시글 수정
    @Transactional
    public PostResponse updatePost(Long postNo, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new CustomException("해당 게시글(postNo: " + postNo + ")을 찾을 수 없습니다."));

        Hibernate.initialize(post);

        post.setTitle(postUpdateRequest.getTitle());
        post.setContent(postUpdateRequest.getContent());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        post.setUpdatedAt(LocalDateTime.now().format(formatter));

        Post updatedPost = postRepository.save(post);

        return new PostResponse(updatedPost, List.of());
    }
    // 게시글 삭제
    @Transactional
    public void deletePost(Long postNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new CustomException("해당 게시글(postNo: " + postNo + ")을 찾을 수 없습니다."));
        // 이미 삭제된 게시글이면 예외 처리
        if (post.getIsDeleted()) {
            throw new CustomException("해당 게시글은 이미 삭제되었습니다.");
        }

        post.setIsDeleted(true);

        postRepository.save(post);
    }

}
