package com.project.recipe.init;

import com.project.recipe.entity.Post;
import com.project.recipe.entity.User;
import com.project.recipe.repository.PostRepository;
import com.project.recipe.repository.UserRepository;
import com.project.recipe.service.PostService;
import com.project.recipe.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostConstruct
    public void init() {

        User user1 = new User("이순신", "거북선123");
        User user2 = new User("김수로", "하룻강아지123");

        userService.saveAllUsers(Arrays.asList(user1, user2));


    }
}