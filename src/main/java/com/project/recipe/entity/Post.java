package com.project.recipe.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorNo", referencedColumnName = "userNo", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String category;

    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private Set<Comment> comments = new LinkedHashSet<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PrePersist
    protected void onCreate() {
        String now = LocalDateTime.now().format(FORMATTER);
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().format(FORMATTER);
    }

    public Post(User author, String title, String content, String category) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.category = category;
        this.isDeleted = false;
    }
}