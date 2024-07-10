package com.example.blogproject.global.controller;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PostService postService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 9, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> postPage = postService.getPublicPosts(pageRequest);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("hasNext", postPage.hasNext());
        model.addAttribute("hasPrevious", postPage.hasPrevious());

        return "index";
    }
}
