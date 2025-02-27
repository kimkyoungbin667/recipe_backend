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
    public ResponseEntity<?> getRecipeById(
            @RequestParam(required = false) Long userNo, // 사용자의 ID (로그인한 경우 전달)
            @PathVariable Long recipeNo) {

        if (recipeNo <= 0) {
            throw new CustomException("올바른 레시피 번호(recipeNo)를 입력해주세요.");
        }

        return ResponseEntity.ok(new ResponseMessage(
                200,
                "레시피 조회 성공",
                recipeService.getRecipeById(userNo, recipeNo) // ✅ userNo 추가
        ));
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
    public ResponseEntity<?> likeRecipe(
            @RequestParam Long userNo, // 사용자의 ID를 프론트에서 전달
            @PathVariable Long recipeNo) {

        recipeService.likeRecipe(userNo, recipeNo);
        return ResponseEntity.ok(new ResponseMessage(200, "좋아요가 추가되었습니다.", null));
    }

    // 좋아요 취소
    @DeleteMapping("/{recipeNo}/like")
    public ResponseEntity<?> unlikeRecipe(
            @RequestParam Long userNo, // 사용자의 ID를 프론트에서 전달
            @PathVariable Long recipeNo) {

        recipeService.unlikeRecipe(userNo, recipeNo);
        return ResponseEntity.ok(new ResponseMessage(200, "좋아요가 취소되었습니다.", null));
    }

    // 인기 레시피 조회 (TOP 5)
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularRecipes() {

        List<RecipeResponse> recipes = recipeService.getTop5PopularRecipes();
        if (recipes.isEmpty()) {
            return ResponseEntity.ok(new ResponseMessage(200, "현재 인기 레시피가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok(new ResponseMessage(200, "인기 레시피 조회 성공", recipes));
    }
}