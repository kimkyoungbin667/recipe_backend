package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_recipe_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_no", "recipe_no"}) // 동일한 사용자가 같은 레시피에 중복 좋아요 불가
})
public class RecipeLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 좋아요 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User user; // 좋아요를 누른 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_no", nullable = false)
    private Recipe recipe; // 좋아요가 눌린 레시피

    public RecipeLike(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }
}