package com.example.blogproject.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/api/posts/{postId}/like")
    @ResponseBody
    public ResponseEntity<String> toggleLike(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String username = authentication.getName();
        boolean isLiked = likesService.toggleLike(postId, username);
        return ResponseEntity.ok(String.valueOf(isLiked));  // boolean을 문자열로 변환
    }

    // 좋아요
    @GetMapping("/@{username}/{postId}/like")
    public String toggleLike(@PathVariable("username") String username,
                             @PathVariable("postId") Long postId,
                             Authentication authentication) {
        if (authentication == null) return "redirect:/login";   // 로그인한 유저인지 확인

        likesService.toggleLike(postId, authentication.getName());

        return "redirect:/@" + username + "/" + postId;
    }
}