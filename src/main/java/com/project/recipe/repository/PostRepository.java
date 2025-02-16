package com.project.recipe.repository;

import com.project.recipe.dto.PostListResponse;
import com.project.recipe.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
    // isDeleted 컬럼이 False인 데이터값만 반환
    Page<Post> findByIsDeletedFalse(Pageable pageable);


}
