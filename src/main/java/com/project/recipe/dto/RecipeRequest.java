package com.project.recipe.dto;

import com.project.recipe.enums.RecipeCategory;
import com.project.recipe.enums.RecipeDifficulty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeRequest {
    private Long authorNo;
    private String title;
    private String description;
    private RecipeCategory recipeCategory;
    private RecipeDifficulty recipeDifficulty;
    private String mainImageUrl;
    private List<IngredientRequest> ingredients;
    private List<RecipeStepRequest> steps;
    private List<RecipeImageRequest> images;
    private String tip;
}