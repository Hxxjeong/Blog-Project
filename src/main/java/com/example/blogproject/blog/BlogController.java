package com.example.blogproject.blog;

import com.example.blogproject.post.Post;
import com.example.blogproject.post.PostService;
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

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/@{name}")
    public String userBlog(@PathVariable("name") String name,
                           @RequestParam(defaultValue = "0") int page,
                           Model model,
                           Authentication authentication) {
        User user = userService.findByUsername(name);
        Blog blog = blogService.findBlog(user.getId());

        // 로그인한 유저인지 확인
        String loginName = authentication != null ? authentication.getName() : null;

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> posts = postService.getPosts(user.getId(), loginName, pageRequest);

        model.addAttribute("blog", blog);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);

        return "/blog/posts";
    }
}
