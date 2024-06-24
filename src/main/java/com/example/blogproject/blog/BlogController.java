package com.example.blogproject.blog;

import com.example.blogproject.post.Post;
import com.example.blogproject.post.PostService;
import com.example.blogproject.user.User;
import com.example.blogproject.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                           Model model) {
        User user = userService.findUser(name);
        Blog blog = blogService.findBlog(user.getId());

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> posts = postService.getPosts(user.getId(), pageRequest);

        model.addAttribute("blog", blog);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);

        return "/blog/posts";
    }
}
