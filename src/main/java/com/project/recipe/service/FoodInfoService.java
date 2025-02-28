package com.project.recipe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodInfoService {

    private final WebClient webClient;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public FoodInfoService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com/v1/search/shop.json")
                .build();
    }

    public Mono<List<Map<String, Object>>> searchTop10LowestProducts(String keyword) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", keyword)
                        .queryParam("display", 50) // 더 많은 데이터를 가져와서 가격 검증
                        .build())
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::getTop10ValidProducts);
    }

    private List<Map<String, Object>> getTop10ValidProducts(Map<String, Object> response) {
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

        List<Map<String, Object>> validItems = items.stream()

                .filter(item -> {
                    int price = Integer.parseInt((String) item.get("lprice"));
                    return price >= 500;  // 500원 미만 제품 제외
                })

                .sorted((item1, item2) -> Integer.compare(
                        Integer.parseInt((String) item1.get("lprice")),
                        Integer.parseInt((String) item2.get("lprice"))
                ))

                .limit(10)
                .collect(Collectors.toList());

        System.out.println("✅ 최종 선택된 상품 개수: " + validItems.size());

        return validItems;
    }
}