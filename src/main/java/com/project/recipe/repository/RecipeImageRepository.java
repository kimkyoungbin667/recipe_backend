package com.project.recipe.repository;

import com.project.recipe.entity.Recipe;
import com.project.recipe.entity.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
    void deleteByRecipe(Recipe recipe);
}
