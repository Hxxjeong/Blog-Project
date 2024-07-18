package com.example.blogproject.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInfoDto {  // 회원정보
    private String name;
    private String blogName;
    private String email;
}
