package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.RecipeRequest;
import com.project.recipe.dto.RecipeResponse;
import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // 레시피 등록
    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody RecipeRequest request) {

        if (request.getAuthorNo() == null) {
            throw new CustomException("레시피 작성자의 번호(authorNo)가 전달되지 않았습니다.");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new CustomException("레시피 제목(title)이 전달되지 않았습니다.");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new CustomException("레시피 설명(description)이 전달되지 않았습니다.");
        }
        if (request.getRecipeCategory() == null) {
            throw new CustomException("레시피 카테고리(recipeCategory)가 전달되지 않았습니다.");
        }
        if (request.getRecipeDifficulty() == null) {
            throw new CustomException("레시피 난이도(recipeDifficulty)가 전달되지 않았습니다.");
        }
        if (request.getMainImageUrl() == null || request.getMainImageUrl().trim().isEmpty()) {
            throw new CustomException("레시피 대표 이미지(mainImageUrl)가 전달되지 않았습니다.");
        }
        if (request.getIngredients() == null || request.getIngredients().isEmpty()) {
            throw new CustomException("레시피 재료 목록(ingredients)이 최소 1개 이상 필요합니다.");
        }
        if (request.getSteps() == null || request.getSteps().isEmpty()) {
            throw new CustomException("레시피 요리 순서(steps)가 최소 1개 이상 필요합니다.");
        }
        if (request.getImages() == null || request.getImages().isEmpty()) {
            throw new CustomException("레시피 요리 완성 이미지(images)가 최소 1개 이상 필요합니다.");
        }

        RecipeResponse createdRecipe = recipeService.createRecipe(request);
        return ResponseEntity.ok(new ResponseMessage(200, "레시피가 성공적으로 등록되었습니다.", createdRecipe));
    }

    // 레시피 목록 조회 (페이징 포함)
    @GetMapping
    public ResponseEntity<?> getRecipes(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "createdAt,DESC") String sort) {
        return ResponseEntity.ok(new ResponseMessage(200, "레시피 목록 조회 성공", recipeService.getRecipes(page, size, sort)));
    }

    // 특정 레시피 조회
    @GetMapping("/{recipeNo}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long recipeNo) {
        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }
        return ResponseEntity.ok(new ResponseMessage(200, "레시피 조회 성공", recipeService.getRecipeById(recipeNo)));
    }

    // 레시피 수정
    @PutMapping("/{recipeNo}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long recipeNo, @RequestBody RecipeRequest request) {

        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }

        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new CustomException("레시피 제목(title)이 전달되지 않았습니다.");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new CustomException("레시피 설명(description)이 전달되지 않았습니다.");
        }
        if (request.getRecipeCategory() == null) {
            throw new CustomException("레시피 카테고리(recipeCategory)가 전달되지 않았습니다.");
        }
        if (request.getRecipeDifficulty() == null) {
            throw new CustomException("레시피 난이도(recipeDifficulty)가 전달되지 않았습니다.");
        }
        if (request.getMainImageUrl() == null || request.getMainImageUrl().trim().isEmpty()) {
            throw new CustomException("레시피 대표 이미지(mainImageUrl)가 전달되지 않았습니다.");
        }
        if (request.getIngredients() == null || request.getIngredients().isEmpty()) {
            throw new CustomException("레시피 재료 목록(ingredients)이 최소 1개 이상 필요합니다.");
        }
        if (request.getSteps() == null || request.getSteps().isEmpty()) {
            throw new CustomException("레시피 요리 순서(steps)가 최소 1개 이상 필요합니다.");
        }
        if (request.getImages() == null || request.getImages().isEmpty()) {
            throw new CustomException("레시피 요리 완성 이미지(images)가 최소 1개 이상 필요합니다.");
        }

        return ResponseEntity.ok(new ResponseMessage(200, "레시피가 성공적으로 수정되었습니다.", recipeService.updateRecipe(recipeNo, request)));
    }

    // 레시피 삭제
    @DeleteMapping("/{recipeNo}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeNo) {

        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }

        recipeService.deleteRecipe(recipeNo);
        return ResponseEntity.ok(new ResponseMessage(200, "레시피가 성공적으로 삭제되었습니다.", null));
    }

    // 좋아요 추가
    @PostMapping("/{recipeNo}/like")
    public ResponseEntity<?> likeRecipe(@PathVariable Long recipeNo) {

        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }

        recipeService.likeRecipe(recipeNo);
        return ResponseEntity.ok(new ResponseMessage(200, "레시피에 좋아요를 추가했습니다.", null));
    }

    // 좋아요 취소
    @PostMapping("/{recipeNo}/unlike")
    public ResponseEntity<?> unlikeRecipe(@PathVariable Long recipeNo) {

        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }
        recipeService.unlikeRecipe(recipeNo);
        return ResponseEntity.ok(new ResponseMessage(200, "레시피 좋아요를 취소했습니다.", null));
    }

    // 인기 레시피 조회 (TOP 5)
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularRecipes() {
        return ResponseEntity.ok(new ResponseMessage(200, "인기 레시피 조회 성공", recipeService.getTop5PopularRecipes()));
    }
}