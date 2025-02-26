package com.project.recipe.service;

import com.project.recipe.dto.ReportRequest;
import com.project.recipe.dto.ReportResponse;
import com.project.recipe.entity.Comment;
import com.project.recipe.entity.Post;
import com.project.recipe.entity.Report;
import com.project.recipe.entity.User;
import com.project.recipe.repository.CommentRepository;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.ReportRepository;
import com.project.recipe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository,
                         PostRepository postRepository, CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ReportResponse createReport(ReportRequest request) {
        User reporter = userRepository.findById(request.getReporterNo())
                .orElseThrow(() -> new IllegalArgumentException("신고자를 찾을 수 없습니다."));

        Report report;

        if (request.getReportedPostNo() != null) {
            Post reportedPost = postRepository.findById(request.getReportedPostNo())
                    .orElseThrow(() -> new IllegalArgumentException("신고 대상 게시글을 찾을 수 없습니다."));
            report = new Report(reporter, reportedPost, request.getReason());
        } else if (request.getReportedCommentNo() != null) {
            Comment reportedComment = commentRepository.findById(request.getReportedCommentNo())
                    .orElseThrow(() -> new IllegalArgumentException("신고 대상 댓글을 찾을 수 없습니다."));
            report = new Report(reporter, reportedComment, request.getReason());
        } else {
            throw new IllegalArgumentException("신고 대상이 지정되지 않았습니다.");
        }

        Report savedReport = reportRepository.save(report);
        return new ReportResponse(savedReport);
    }

    @Transactional(readOnly = true)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}