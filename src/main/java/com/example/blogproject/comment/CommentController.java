package com.example.blogproject.comment;

import com.example.blogproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/@{username}/{postId}/comment")
    public String addComment(@PathVariable String username,
                             @PathVariable Long postId,
                             @RequestParam String content,
                             Authentication authentication) {
        String loginUsername = authentication.getName();
        commentService.addComment(postId, loginUsername, content);
        return "redirect:/@" + username + "/" + postId;
    }
}
