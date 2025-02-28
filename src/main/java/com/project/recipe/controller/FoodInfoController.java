package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.service.FoodInfoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foodinfo")
public class FoodInfoController {

    private final FoodInfoService foodInfoService;

    public FoodInfoController(FoodInfoService foodInfoService) {
        this.foodInfoService = foodInfoService;
    }

    @GetMapping("/search")
    public Mono<List<Map<String, Object>>> searchLowestProducts(@RequestParam String keyword) {

        return foodInfoService.searchTop10LowestProducts(keyword);
    }
}