package com.project.recipe.dto;

import com.project.recipe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long userNo;
    private String name;
    private String nickname;

    public AuthorResponse(User user) {
        this.userNo = user.getUserNo();
        this.name = user.getName();
        this.nickname = user.getNickname();
    }
}