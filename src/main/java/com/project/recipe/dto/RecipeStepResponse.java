package com.project.recipe.dto;

import com.project.recipe.entity.RecipeStep;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeStepResponse {
    private Long stepNo;
    private String description;
    private String imageUrl;

    public RecipeStepResponse(RecipeStep step) {
        this.stepNo = step.getStepNo();
        this.description = step.getDescription();
        this.imageUrl = step.getImageUrl();
    }
}