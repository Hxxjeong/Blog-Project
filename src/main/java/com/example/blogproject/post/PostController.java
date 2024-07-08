package com.example.blogproject.post;

import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;

    // 포스트 작성
    @GetMapping("/write")
    public String postForm(Authentication authentication, Model model) {
        // 로그인한 사용자인지 확인
        String username = authentication.getName();
        log.info("username = {}", authentication.getName());
        if(authentication == null || username == null) return "redirect:/loginform";

        model.addAttribute("username", authentication.getName());

        return "blog/postform";
    }

    @PostMapping("/post")
    public String create(@RequestParam("title") String title,
                         @RequestParam("tags") String tags,
                         @RequestParam("content") String content,
                         @RequestParam("image")MultipartFile image,
                         @RequestParam("isSecret") boolean isSecret,
                         @RequestParam("isTemp") boolean isTemp,
                         Authentication authentication,
                         Model model) {
        // 로그인한 유저인지 확인
        String username = authentication.getName();
        if(authentication == null || username == null) return "redirect:/loginform";

        User user = userService.findByUsername(authentication.getName());
        String blogName = user.getBlog().getName();
        model.addAttribute("user", user);

        // 태그 쉼표로 구분
        List<String> tagNames = Arrays.asList(tags.split(","));

        // 포스트 저장
        Post post = postService.create(user.getId(), title, content, image, isSecret, isTemp, tagNames);
        model.addAttribute("post", post);

        return "redirect:/@" + blogName;
    }

    @GetMapping("/@{name}/{title}")
    public String getPost(@PathVariable("name") String name, @PathVariable("title") String title) {

        return "blog/postDetail";
    }
}
