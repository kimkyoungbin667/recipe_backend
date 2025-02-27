package com.project.recipe.service;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.RecipeRequest;
import com.project.recipe.dto.RecipeResponse;
import com.project.recipe.entity.*;
import com.project.recipe.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeImageRepository recipeImageRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository,
                         IngredientRepository ingredientRepository, RecipeStepRepository recipeStepRepository,
                         RecipeImageRepository recipeImageRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.recipeImageRepository = recipeImageRepository;
    }

    // 레시피 등록
    @Transactional
    public RecipeResponse createRecipe(RecipeRequest request) {
        User author = userRepository.findById(request.getAuthorNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 작성자를 찾을 수 없습니다."));

        // 먼저 Recipe 저장
        final Recipe savedRecipe = recipeRepository.save(
                new Recipe(author, request.getTitle(), request.getDescription(),
                        request.getRecipeCategory(), request.getRecipeDifficulty(), request.getMainImageUrl())
        );

        List<Ingredient> ingredients = request.getIngredients().stream()
                .map(i -> new Ingredient(savedRecipe, i.getName(), i.getQuantity(), i.getNote())) // ✅ Recipe 객체가 첫 번째로 와야 함!
                .collect(Collectors.toList());
        savedRecipe.setIngredients(ingredients);
        ingredientRepository.saveAll(ingredients);

        List<RecipeStep> steps = request.getSteps().stream()
                .map(s -> new RecipeStep(savedRecipe, s.getDescription(), s.getImageUrl()))
                .collect(Collectors.toList());
        savedRecipe.setSteps(steps);
        recipeStepRepository.saveAll(steps);

        List<RecipeImage> images = request.getImages().stream()
                .map(i -> new RecipeImage(savedRecipe, i.getImageUrl()))
                .collect(Collectors.toList());
        savedRecipe.setImages(images);
        recipeImageRepository.saveAll(images);

        return new RecipeResponse(savedRecipe);
    }

    // 레시피 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<RecipeResponse> getRecipes(int page, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Recipe> recipes = recipeRepository.findAllByIsDeletedFalse(pageRequest);
        return recipes.map(RecipeResponse::new);
    }

    // 특정 레시피 조회
    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(Long recipeNo) {
        Recipe recipe = recipeRepository.findById(recipeNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));
        return new RecipeResponse(recipe);
    }

    // 레시피 수정
    @Transactional
    public RecipeResponse updateRecipe(Long recipeNo, RecipeRequest request) {
        Recipe recipe = recipeRepository.findById(recipeNo)
                .orElseThrow(() -> new CustomException("해당 레시피를 찾을 수 없습니다."));

        // 기본 정보 업데이트
        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setRecipeCategory(request.getRecipeCategory());
        recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        recipe.setMainImageUrl(request.getMainImageUrl());
        recipe.setTip(request.getTip());

        // ✅ 기존 재료 삭제 후 새로운 값으로 변경
        ingredientRepository.deleteByRecipe(recipe); // 기존 재료 삭제
        List<Ingredient> newIngredients = request.getIngredients().stream()
                .map(dto -> new Ingredient(recipe, dto.getName(), dto.getQuantity(), dto.getNote()))
                .collect(Collectors.toList());
        recipe.getIngredients().clear();
        recipe.getIngredients().addAll(newIngredients);


        recipeStepRepository.deleteByRecipe(recipe);
        List<RecipeStep> newSteps = request.getSteps().stream()
                .map(dto -> new RecipeStep(recipe, dto.getDescription(), dto.getImageUrl()))
                .collect(Collectors.toList());
        recipe.getSteps().clear();
        recipe.getSteps().addAll(newSteps);

        recipeImageRepository.deleteByRecipe(recipe);
        List<RecipeImage> newImages = request.getImages().stream()
                .map(dto -> new RecipeImage(recipe, dto.getImageUrl()))
                .collect(Collectors.toList());
        recipe.getImages().clear();
        recipe.getImages().addAll(newImages);

        // 변경된 데이터 저장
        recipeRepository.save(recipe);

        return new RecipeResponse(recipe);
    }

    // 레시피 삭제
    @Transactional
    public void deleteRecipe(Long recipeNo) {
        Recipe recipe = recipeRepository.findById(recipeNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));

        // 이미 삭제된 게시글이면 예외 처리
        if (recipe.isDeleted()) {
            throw new CustomException("해당 레시피는 이미 삭제되었습니다.");
        }

        recipe.deleteRecipe();
        recipeRepository.save(recipe);
    }

    // 좋아요 추가
    @Transactional
    public void likeRecipe(Long recipeNo) {
        Recipe recipe = recipeRepository.findById(recipeNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));

        recipe.increaseLikeCount();
        recipeRepository.save(recipe);
    }

    // 좋아요 취소
    @Transactional
    public void unlikeRecipe(Long recipeNo) {
        Recipe recipe = recipeRepository.findById(recipeNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));

        recipe.decreaseLikeCount();
        recipeRepository.save(recipe);
    }

    // 인기 레시피 조회 (TOP 5)
    @Transactional(readOnly = true)
    public List<RecipeResponse> getTop5PopularRecipes() {
        List<Recipe> topRecipes = recipeRepository.findTop5ByOrderByLikeCountDesc();
        return topRecipes.stream().map(RecipeResponse::new).collect(Collectors.toList());
    }
}