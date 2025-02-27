package com.project.recipe.dto;

import com.project.recipe.entity.RecipeImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeImageResponse {
    private String imageUrl;

    public RecipeImageResponse(RecipeImage image) {
        this.imageUrl = image.getImageUrl();
    }
}