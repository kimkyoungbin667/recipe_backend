package com.project.recipe.dto;

import com.project.recipe.entity.Report;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {
    private Long reportNo;
    private Long reporterNo;
    private Long reportedPostNo;
    private Long reportedCommentNo;
    private String reason;
    private String createdAt;

    public ReportResponse(Report report) {
        this.reportNo = report.getReportNo();
        this.reporterNo = report.getReporter().getUserNo();
        this.reportedPostNo = report.getReportedPost() != null ? report.getReportedPost().getPostNo() : null;
        this.reportedCommentNo = report.getReportedComment() != null ? report.getReportedComment().getCommentNo() : null;
        this.reason = report.getReason();
        this.createdAt = report.getCreatedAt();
    }
}