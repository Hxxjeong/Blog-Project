package com.example.blogproject.post.controller;

import com.example.blogproject.global.util.MarkdownUtils;
import com.example.blogproject.likes.LikesService;
import com.example.blogproject.post.dto.PostUpdateDto;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.service.PostService;
import com.example.blogproject.tag.entity.Tag;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final LikesService likesService;

    // 포스트 작성
    @GetMapping("/write")
    public String postForm(Authentication authentication, Model model) {
        // 로그인한 사용자인지 확인
        String username = authentication.getName();
        if (authentication == null || username == null) return "redirect:/loginform";

        model.addAttribute("username", authentication.getName());

        return "blog/postform";
    }

    @PostMapping("/post")
    public String create(@RequestParam("title") String title,
                         @RequestParam("tags") String tags,
                         @RequestParam("content") String content,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("isSecret") boolean isSecret,
                         @RequestParam("isTemp") boolean isTemp,
                         Authentication authentication,
                         Model model) {
        // 로그인한 유저인지 확인
        String username = authentication.getName();
        if (authentication == null || username == null) return "redirect:/loginform";

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            // 제목 또는 내용이 비어있을 경우 에러 처리
            model.addAttribute("error", "제목과 내용을 입력해주세요.");
            return "blog/postform";
        }

        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);

        // 태그 쉼표로 구분
        List<String> tagNames = Arrays.asList(tags.split(","));

        // 포스트 저장
        Post post = postService.create(user.getId(), title, content, image, isSecret, isTemp, tagNames);
        model.addAttribute("post", post);

        return "redirect:/@" + username;
    }

    // 게시물 한 개 조회
    @GetMapping("/@{username}/{postId}")
    public String getPost(@PathVariable("username") String username,
                          @PathVariable("postId") Long postId,
                          Model model,
                          Authentication authentication) {
        Post post = postService.getPostById(postId);
        String content = MarkdownUtils.markdownToHtml(post.getContent());

        // 비공개 글 확인
        if(post.isSecret()) {
            if(authentication == null) return "redirect:/";

            // 로그인한 사용자 확인
            String loginUsername = authentication.getName();
            if(!post.getUser().getUsername().equals(loginUsername)) return "redirect:/";    // 권한 없음
        }

        boolean isLiked = false;
        if (authentication != null) {
            isLiked = likesService.isPostLikedByUser(post, authentication.getName());
        }

        model.addAttribute("post", post);
        model.addAttribute("htmlContent", content);
        model.addAttribute("isLiked", isLiked);
        return "blog/postDetail";
    }

    // 게시글 수정
    @GetMapping("/@{username}/{postId}/edit")
    public String updateForm (@PathVariable("username") String username,
                              @PathVariable("postId") Long postId,
                              Authentication authentication,
                              Model model) {
        // 로그인한 사용자 확인
        Post post = postService.getPostById(postId);

        String loginUser = authentication.getName();
        if(!post.getUser().getUsername().equals(loginUser)) return "redirect:/";

        PostUpdateDto updateDto = PostUpdateDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .isSecret(post.isSecret())
                .isTemp(post.isTemp())
                .tags(post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .build();

        model.addAttribute("username", username);
        model.addAttribute("postId", postId);
        model.addAttribute("postUpdateDTO", updateDto);
        model.addAttribute("currentImage", post.getImage()); // 현재 이미지 정보

        return "blog/postedit";
    }

    @PostMapping("/@{username}/{postId}/edit")
    public String update(@PathVariable("username") String username,
                         @PathVariable("postId") Long postId,
                         @ModelAttribute PostUpdateDto updateDto,
                         Authentication authentication) throws IOException {
        Post post = postService.getPostById(postId);

        String loginUser = authentication.getName();
        if(!post.getUser().getUsername().equals(loginUser)) return "redirect:/";

        postService.update(postId, updateDto);

        return "redirect:/@" + username + "/" + postId;
    }

    // 게시물 삭제
    @PostMapping("/@{username}/{postId}/delete")
    public String delete(@PathVariable("username") String username,
                         @PathVariable("postId") Long postId,
                         Authentication authentication) {
        Post post = postService.getPostById(postId);

        String loginUser = authentication.getName();
        if(!post.getUser().getUsername().equals(loginUser)) return "redirect:/";

        postService.delete(postId, username);

        return "redirect:/@" + username;
    }
}
