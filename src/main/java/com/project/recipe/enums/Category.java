package com.project.recipe.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

// 카테고리 종류
public enum Category {
    QUESTION("질문"),
    SHARE("정보 공유"),
    REVIEW("후기"),
    NOTICE("공지사항"),
    GREET("가입인사");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
