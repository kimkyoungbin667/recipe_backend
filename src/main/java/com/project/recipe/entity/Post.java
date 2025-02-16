package com.project.recipe.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="tb_post")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post { // 게시글 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // PK 자동 증가
    private Long postNo;  // 게시글 번호

    @ManyToOne(fetch = FetchType.LAZY) // User 테이블과 연결 (필요시에 연결함-LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "authorNo", referencedColumnName = "userNo", nullable = false)
    private User author;  // 게시글 작성자 정보

    @Column(nullable = false)
    private String title;  // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;  // 게시글 내용

    @Column(nullable = false)
    private String category; // 카테고리

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now().withNano(0); // 게시글 작성일 (마이크로초는 0으로)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now().withNano(0); // 게시글 수정일 (마이크로초는 0으로)

    private Boolean isDeleted = false;  // 게시글 삭제 여부

    public Post(User author, String category, String title, String content) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

}
