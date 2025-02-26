package com.project.recipe.controller;

import com.project.recipe.config.CustomException;
import com.project.recipe.dto.ReportRequest;
import com.project.recipe.dto.ReportResponse;
import com.project.recipe.dto.ResponseMessage;
import com.project.recipe.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 게시글 또는 댓글 신고
    @PostMapping
    public ResponseEntity<?> reportContent(@RequestBody ReportRequest request) {

        if (request.getReporterNo() == null) {
            throw new CustomException("신고자 번호가 전달되지 않았습니다.");
        }

        if (request.getReportedPostNo() == null && request.getReportedCommentNo() == null) {
            throw new CustomException("신고 대상(게시글 또는 댓글)이 전달되지 않았습니다.");
        }

        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new CustomException("신고 사유가 전달되지 않았습니다.");
        }

        ReportResponse reportResponse = reportService.createReport(request);
        return ResponseEntity.ok(new ResponseMessage(200, "신고가 접수되었습니다.", reportResponse));
    }

    // 신고 목록 조회 (관리자용)
    @GetMapping
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(new ResponseMessage(200, "신고 목록 조회 성공", reportService.getAllReports()));
    }
}