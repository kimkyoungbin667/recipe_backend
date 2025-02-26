package com.project.recipe.dto;

import com.project.recipe.entity.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RecipeResponse {
    private Long recipeNo;
    private String title;
    private String description;
    private String mainImageUrl;
    private int likeCount;
    private String recipeCategory;
    private String recipeDifficulty;

    private List<IngredientResponse> ingredients;
    private List<RecipeStepResponse> steps;
    private List<RecipeImageResponse> images;

    public RecipeResponse(Recipe recipe) {
        this.recipeNo = recipe.getRecipeNo();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.mainImageUrl = recipe.getMainImageUrl();
        this.likeCount = recipe.getLikeCount();
        this.recipeCategory = recipe.getRecipeCategory().name();
        this.recipeDifficulty = recipe.getRecipeDifficulty().name();

        this.ingredients = recipe.getIngredients().stream()
                .map(IngredientResponse::new)
                .collect(Collectors.toList());

        this.steps = recipe.getSteps().stream()
                .map(RecipeStepResponse::new)
                .collect(Collectors.toList());

        this.images = recipe.getImages().stream()
                .map(RecipeImageResponse::new)
                .collect(Collectors.toList());
    }
}