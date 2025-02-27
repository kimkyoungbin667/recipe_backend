package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "tb_ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeNo", nullable = false)
    private Recipe recipe; // ✅ Recipe 객체 참조

    @Column(nullable = false)
    private String name; // 재료 이름

    @Column(nullable = false)
    private String quantity; // 얼만큼인지 (ml, g 등)

    private String note; // 비고 (선택 사항)

    public Ingredient(Recipe recipe, String name, String quantity, String note) {
        this.recipe = recipe;
        this.name = name;
        this.quantity = quantity;
        this.note = note;
    }
}