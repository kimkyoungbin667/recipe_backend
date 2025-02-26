package com.project.recipe.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;

public enum RecipeDifficulty {
    EASY("쉬움"),
    MEDIUM("보통"),
    HARD("어려움");

    private final String displayName;

    RecipeDifficulty(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static RecipeDifficulty fromString(String value) {
        return Arrays.stream(RecipeDifficulty.values())
                .filter(difficulty -> difficulty.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 난이도: " + value));
    }

}