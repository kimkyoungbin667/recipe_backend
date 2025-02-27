package com.project.recipe.repository;

import com.project.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // 전체 레시피 조회 (페이징 적용)
    Page<Recipe> findAllByIsDeletedFalse(Pageable pageable);

    // 좋아요 수 기준으로 상위 5개 인기 레시피 조회
    List<Recipe> findTop5ByOrderByLikeCountDesc();

    @EntityGraph(attributePaths = {"ingredients", "steps", "images"})
    Optional<Recipe> findByRecipeNoAndIsDeletedFalse(Long recipeNo);

    List<Recipe> findTop5ByIsDeletedFalseOrderByLikeCountDesc();

}