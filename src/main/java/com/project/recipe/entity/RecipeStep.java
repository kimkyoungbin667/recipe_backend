package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_recipe_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeNo", nullable = false)
    private Recipe recipe;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // 스텝 설명

    private String imageUrl; // 스텝 사진 (선택 사항)

    public RecipeStep(Recipe recipe, String description, String imageUrl) {
        this.recipe = recipe;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}