package com.project.recipe.service;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.CommentCreateRequest;
import com.project.recipe.dto.CommentResponse;
import com.project.recipe.entity.Comment;
import com.project.recipe.entity.Post;
import com.project.recipe.entity.User;
import com.project.recipe.repository.CommentRepository;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 작성
    public CommentResponse createComment(CommentCreateRequest request) {
        Post post = postRepository.findById(request.getPostNo())
                .orElseThrow(() -> new CustomException("해당 게시글을 찾을 수 없습니다."));
        User author = userRepository.findById(request.getAuthorNo())
                .orElseThrow(() -> new CustomException("해당 작성자를 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(request.getContent());

        // 생성 시 현재 시간을 yyyy-MM-dd HH:mm:ss 형식으로 설정
        comment.setCreatedAt(LocalDateTime.now().format(FORMATTER));
        comment.setUpdatedAt(LocalDateTime.now().format(FORMATTER));

        if (request.getParentCommentNo() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentNo())
                    .orElseThrow(() -> new CustomException("해당 부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponse(savedComment, List.of());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentNo) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new CustomException("해당 댓글을 찾을 수 없습니다."));

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }
}