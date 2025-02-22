package com.project.recipe.repository;

import com.project.recipe.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiChatRepository extends JpaRepository<AiChat, Long> {
}
