package com.example.blogproject.blog;

import com.example.blogproject.global.util.MarkdownUtils;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.service.PostService;
import com.example.blogproject.tag.service.TagService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;

    @GetMapping("/@{name}")
    public String userBlog(@PathVariable("name") String name,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String tag,
                           Model model,
                           Authentication authentication) {
        User user = userService.findByUsername(name);
        Blog blog = blogService.findBlog(user.getId());

        // 로그인한 유저인지 확인
        String loginUser = authentication != null ? authentication.getName() : null;

        Map<String, Long> tagCounts = tagService.getTagCountsByBlogId(blog.getId(), loginUser);

        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> posts;

        if (tag != null && !tag.isEmpty()) {
            posts = postService.getPostsByTag(user.getId(), name, loginUser, tag, pageRequest);
        } else {
            posts = postService.getPosts(user.getId(), name, loginUser, pageRequest);
        }

        // markdown to html
        Map<Long, String> plainContents = new HashMap<>();
        for(Post post: posts.getContent()) {
            String plain = MarkdownUtils.removeMarkdown(post.getContent());
            plainContents.put(post.getId(), plain);
        }

        model.addAttribute("blog", blog);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("plainContents", plainContents);
        model.addAttribute("tagCounts", tagCounts); // 해당 블로그에서 사용한 태그
        model.addAttribute("selectedTag", tag);

        return "/blog/posts";
    }
}
