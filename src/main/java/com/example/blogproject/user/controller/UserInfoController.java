package com.example.blogproject.user.controller;

import com.example.blogproject.user.dto.UserInfoDto;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.service.UserInfoService;
import com.example.blogproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final UserService userService;

    @GetMapping("/setting")
    public String getUserInfo(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .name(user.getName())
                .blogName(user.getBlog().getName())
                .email(user.getEmail())
                .build();
        model.addAttribute("userInfo", userInfoDto);
        return "user/info";
    }

    @PostMapping("/setting")
    public String updateUserInfo(@ModelAttribute UserInfoDto userInfoDto,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(authentication.getName());

            userInfoService.updateUserInfo(userInfoDto, user.getEmail());
            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        }
        catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/setting";
    }
}