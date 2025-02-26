package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tb_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportNo; // 신고 번호 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterNo", referencedColumnName = "userNo", nullable = false)
    private User reporter; // 신고한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedPostNo", nullable = true)
    private Post reportedPost; // 신고 대상이 게시글일 경우

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedCommentNo", nullable = true)
    private Comment reportedComment; // 신고 대상이 댓글일 경우

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason; // 신고 사유

    private String createdAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().format(FORMATTER);
    }

    public Report(User reporter, Post post, String reason) {
        this.reporter = reporter;
        this.reportedPost = post;
        this.reason = reason;
    }

    public Report(User reporter, Comment comment, String reason) {
        this.reporter = reporter;
        this.reportedComment = comment;
        this.reason = reason;
    }
}