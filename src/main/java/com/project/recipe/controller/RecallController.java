package com.project.recipe.controller;

import com.project.recipe.service.RecallService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/recalls")
public class RecallController {

    private final RecallService recallService;

    public RecallController(RecallService recallService) {
        this.recallService = recallService;
    }

    @GetMapping
    public Mono<Map> getRecallInfo(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int cntPerPage) {


        return recallService.getRecallInfo(pageNo, cntPerPage);
    }

}