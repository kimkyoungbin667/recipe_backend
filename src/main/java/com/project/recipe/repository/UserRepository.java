package com.project.recipe.repository;

import com.project.recipe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    Optional<User> findByUserNo(Long userNo);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsById(String id);  // 아이디 중복 체크
    boolean existsByNickname(String nickname);  // 닉네임 중복 체크
    User findByEmailAndId(String email, String Id);
}