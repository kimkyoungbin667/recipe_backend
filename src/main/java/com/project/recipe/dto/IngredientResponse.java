package com.project.recipe.dto;

import com.project.recipe.entity.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientResponse {
    private String name;
    private String quantity;
    private String note;

    public IngredientResponse(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.quantity = ingredient.getQuantity();
        this.note = ingredient.getNote();
    }
}