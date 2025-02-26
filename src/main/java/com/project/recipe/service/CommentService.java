package com.project.recipe.service;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.CommentCreateRequest;
import com.project.recipe.dto.CommentResponse;
import com.project.recipe.dto.CommentUpdateRequest;
import com.project.recipe.entity.Comment;
import com.project.recipe.entity.Post;
import com.project.recipe.entity.User;
import com.project.recipe.repository.CommentRepository;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

        String now = LocalDateTime.now().format(FORMATTER);
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);
        comment.setIsDeleted(false);

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
                .orElseThrow(() -> new CustomException("해당 댓글(대댓글)을 찾을 수 없습니다."));

        if (Boolean.TRUE.equals(comment.getIsDeleted())) {
            throw new CustomException("해당 댓글(대댓글)은 이미 삭제되었습니다.");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long commentNo, CommentUpdateRequest request) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new CustomException("수정할 댓글(대댓글)을 찾을 수 없습니다."));

        // 댓글 내용 수정
        comment.setContent(request.getContent());
        commentRepository.save(comment);

        // 대댓글을 CommentResponse 형태로 변환
        List<CommentResponse> childResponses = comment.getChildComments().stream()
                .map(child -> new CommentResponse(child, null)) // 대댓글의 하위 대댓글을 고려하지 않음
                .collect(Collectors.toList());

        // 응답 DTO 반환
        return new CommentResponse(comment, childResponses);
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentNo) {
        // 댓글 조회 (없으면 예외 발생)
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new CustomException("해당 댓글(대댓글)을 찾을 수 없습니다."));

        // 대댓글 포함하여 응답 생성
        List<CommentResponse> childComments = comment.getChildComments().stream()
                .map(child -> new CommentResponse(child, null)) // 대댓글의 하위 대댓글까지 고려하지 않음
                .collect(Collectors.toList());

        return new CommentResponse(comment, childComments);
    }
}