package com.example.blogproject.blog;

import com.example.blogproject.user.User;
import com.example.blogproject.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;

    @GetMapping("/@{name}")
    public String userBlog(@PathVariable("name") String name, Model model) {
        User user = userService.findUser(name);
        Blog blog = blogService.findBlog(user.getId());

        model.addAttribute("blog", blog);
        return "/blog/posts";
    }
}
