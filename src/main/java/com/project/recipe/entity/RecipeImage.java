package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_recipe_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeNo", nullable = false)
    private Recipe recipe;

    @Column(nullable = false)
    private String imageUrl; // 사진 URL

    public RecipeImage(Recipe recipe, String imageUrl) {
        this.recipe = recipe;
        this.imageUrl = imageUrl;
    }
}