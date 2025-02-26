package com.project.recipe.entity;

import com.project.recipe.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;  // 회원번호 (PK)

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자 이내여야 합니다.")
    @Column(unique = true, nullable = false, length = 20)
    private String id; // 아이디

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    @Column(nullable = false)
    private String password; // 비밀번호

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Column(unique = true, nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부 (기본값 false)

    @Column(nullable = false)
    private boolean isActive = true; // 활성화 여부 (기본값 true)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성일

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 50, message = "이름은 2~50자 이내여야 합니다.")
    @Column(nullable = false, length = 50)
    private String name; // 이름

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 20, message = "닉네임은 2~20자 이내여야 합니다.")
    @Column(unique = true, nullable = false, length = 20)
    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)  // Enum 값으로 저장
    @Column(nullable = false)
    private Role role = Role.USER;  // 기본값을 USER로 설정

}