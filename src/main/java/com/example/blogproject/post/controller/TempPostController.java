package com.example.blogproject.post.controller;

import com.example.blogproject.global.util.MarkdownUtils;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.service.PostService;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TempPostController {
    private final PostService postService;
    private final UserService userService;

    // 임시 글 조회
    @GetMapping("/temps")
    public String getTemps(@RequestParam(defaultValue = "0") int page,
                           Model model,
                           Authentication authentication) {
        // 로그인한 유저인지 확인
        if(authentication == null) return "redirect:/loginform";

        String username  = authentication.getName();
        User user = userService.findByUsername(username);

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> temps = postService.getTemps(user.getId(), pageRequest);

        // markdown to html
        Map<Long, String> plainContents = new HashMap<>();
        for(Post post: temps.getContent()) {
            String plain = MarkdownUtils.removeMarkdown(post.getContent());
            plainContents.put(post.getId(), plain);
        }

        model.addAttribute("user", user);
        model.addAttribute("posts", temps);
        model.addAttribute("plainContents", plainContents);

        return "blog/temps";
    }
}
