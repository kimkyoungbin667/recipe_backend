package com.project.recipe.repository;

import com.project.recipe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 삭제되지 않은 게시글 목록 조회
    Page<Post> findByIsDeletedFalse(Pageable pageable);

    // 게시글 상세 조회 시 댓글을 함께 로드
    @EntityGraph(attributePaths = {"comments", "comments.replies"})
    Optional<Post> findByPostNoAndIsDeletedFalse(Long postNo);
}