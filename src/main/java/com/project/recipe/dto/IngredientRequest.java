package com.project.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientRequest {
    private String name;  // 재료 이름
    private String quantity;  // 얼만큼인지 (100g, 1공기 등)
    private String note;  // 비고 (예: 신김치 추천)
}