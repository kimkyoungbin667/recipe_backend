package com.project.recipe.repository;

import com.project.recipe.entity.Ingredient;
import com.project.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    void deleteByRecipe(Recipe recipe);
}