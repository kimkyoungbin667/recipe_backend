package com.project.recipe.repository;

import com.project.recipe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 삭제되지 않은 게시글 목록 조회
    Page<Post> findByIsDeletedFalse(Pageable pageable);

    // 게시글 상세 조회 시 댓글을 함께 로드
    @EntityGraph(attributePaths = {"comments", "comments.replies"})
    Optional<Post> findByPostNoAndIsDeletedFalse(Long postNo);

    // 제목 검색
    Page<Post> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);

    // 내용 검색
    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(FUNCTION('REGEXP_REPLACE', p.content, '<[^>]+>', '')) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND p.isDeleted = false")
    Page<Post> searchByContentIgnoreCaseWithoutHtml(@Param("keyword") String keyword, Pageable pageable);

    // 작성자 닉네임 검색
    Page<Post> findByAuthor_NicknameContainingIgnoreCaseAndIsDeletedFalse(String nickname, Pageable pageable);
}