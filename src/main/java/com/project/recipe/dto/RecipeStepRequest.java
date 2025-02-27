package com.project.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeStepRequest {
    private String description;  // 요리 과정 설명
    private String imageUrl;  // 요리 과정 이미지 URL
}
