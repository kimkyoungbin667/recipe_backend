package com.project.recipe.repository;

import com.project.recipe.entity.Recipe;
import com.project.recipe.entity.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    void deleteByRecipe(Recipe recipe);
}