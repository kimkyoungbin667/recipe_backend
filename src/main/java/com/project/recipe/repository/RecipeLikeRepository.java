package com.project.recipe.repository;

import com.project.recipe.entity.RecipeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    boolean existsByUser_UserNoAndRecipe_RecipeNo(Long userNo, Long recipeNo);
    void deleteByUser_UserNoAndRecipe_RecipeNo(Long userNo, Long recipeNo);
}