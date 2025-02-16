package com.project.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@NoArgsConstructor
public class User { // 임시 유저 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;  // 유저 ID

    @Column(nullable = false)
    private String userName; // 유저 이름

    @Column(nullable = false)
    private String userNickname; // 유저 닉네임

    public User(String userName, String userNickname) {
        this.userName = userName;
        this.userNickname = userNickname;
    }
}