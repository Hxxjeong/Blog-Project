package com.example.blogproject.user.service;

import com.example.blogproject.blog.Blog;
import com.example.blogproject.user.dto.UserInfoDto;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public void updateUserInfo(UserInfoDto userInfoDto, String email) {
        User currentUser = userService.findUserByEmail(email);
        if (currentUser == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        // 이메일 변경 시 중복 검사
        if (!email.equals(userInfoDto.getEmail())) {
            if (userService.findUserByEmail(userInfoDto.getEmail()) != null) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }
        }

        // User 정보 업데이트
        currentUser.setName(userInfoDto.getName());
        currentUser.setEmail(userInfoDto.getEmail());

        // Blog 정보 업데이트
        if (currentUser.getBlog() == null) {
            Blog newBlog = new Blog(userInfoDto.getBlogName(), currentUser);
            currentUser.setBlog(newBlog);
        } else {
            currentUser.getBlog().setName(userInfoDto.getBlogName());
        }

        userRepository.save(currentUser);
    }
}
