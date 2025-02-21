package com.project.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private int status;   // HTTP 상태 코드 (200, 400, 500)
    private String message; // 응답 메시지
    private Object data;  // 응답 데이터 (성공 시 객체 포함, 에러 시 null)
}
