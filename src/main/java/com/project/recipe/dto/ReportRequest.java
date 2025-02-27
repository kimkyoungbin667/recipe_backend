package com.project.recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private Long reporterNo;       // 신고자
    private Long reportedPostNo;   // 신고 대상이 게시글일 경우
    private Long reportedCommentNo; // 신고 대상이 댓글일 경우
    private String reason;         // 신고 사유
}