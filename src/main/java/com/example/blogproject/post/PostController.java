package com.example.blogproject.post;

import com.example.blogproject.user.User;
import com.example.blogproject.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class PostController {
    private final PostService postService;
    private final UserService userService;

    // 포스트 작성
    @GetMapping("/write")
    public String postForm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 로그인한 사용자가 아니면 로그인 페이지로 이동
        if(session == null || session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        return "blog/postform";
    }

    @PostMapping("/post")
    public String create(@RequestParam("title") String title,
                         @RequestParam("tags") String tags,
                         @RequestParam("content") String content,
                         @RequestParam("image")MultipartFile image,
                         HttpServletRequest request) {
        // 로그인한 유저인지 확인
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("username");
        User user = userService.findUser(username);

        // 태그 쉼표로 구분
        List<String> tagNames = Arrays.asList(tags.split(","));

        // 포스트 저장
        postService.create(user.getId(), title, content, image, tagNames);

        return "redirect:/@" + username;
    }
}
