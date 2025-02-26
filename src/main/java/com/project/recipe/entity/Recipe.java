package com.project.recipe.entity;

import com.project.recipe.enums.RecipeCategory;
import com.project.recipe.enums.RecipeDifficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorNo", nullable = false)
    private User author; // 작성자

    @Column(nullable = false)
    private String title; // 레시피 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // 요리 소개

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeCategory recipeCategory; // 요리 분류 (한식, 중식 등)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeDifficulty recipeDifficulty; // 난이도 (쉬움, 보통, 어려움)

    @Column(nullable = false)
    private String mainImageUrl; // 대표 사진 (URL)

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>(); // 재료 목록

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepNo ASC")
    private List<RecipeStep> steps = new ArrayList<>(); // 요리 순서

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeImage> images = new ArrayList<>(); // 요리 완성 사진

    @Column(columnDefinition = "TEXT")
    private String tip; // 요리 팁

    @Column(nullable = false)
    private int likeCount = 0; // 좋아요 수 (기본값 0)

    private String createdAt;
    private String updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().format(FORMATTER);
        this.updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().format(FORMATTER);
    }

    // 좋아요 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 감소
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void deleteRecipe() {
        this.isDeleted = true;
    }

    public Recipe(User author, String title, String description, RecipeCategory recipeCategory,
                  RecipeDifficulty recipeDifficulty, String mainImageUrl) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.recipeCategory = recipeCategory;
        this.recipeDifficulty = recipeDifficulty;
        this.mainImageUrl = mainImageUrl;
        this.likeCount = 0;
    }
}