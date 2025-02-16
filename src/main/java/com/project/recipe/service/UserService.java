package com.project.recipe.service;

import com.project.recipe.entity.User;
import com.project.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 유저 임시 저장
    public void saveAllUsers(List<User> users) {
        userRepository.saveAll(users);
    }
}