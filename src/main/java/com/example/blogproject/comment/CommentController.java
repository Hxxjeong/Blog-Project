package com.example.blogproject.comment;

import com.example.blogproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 댓글 수정
    @PostMapping("/@{username}/{postId}/comment/{commentId}/edit")
    public String updateComment(@PathVariable("username") String username,
                                @PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                @RequestParam("content") String content,
                                Authentication authentication) {
        String loginUser = authentication.getName();
        Comment comment = commentService.getCommentById(commentId);

        // 댓글 작성자만 수정 가능
        if (loginUser.equals(comment.getUser().getUsername())) {
            commentService.updateComment(commentId, content);
        }

        return "redirect:/@" + username + "/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/@{username}/{postId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("username") String username,
                                @PathVariable("postId") Long postId,
                                @PathVariable("commentId") Long commentId,
                                Authentication authentication) {
        String loginUser = authentication.getName();
        Comment comment = commentService.getCommentById(commentId);

        // 댓글 작성자와 게시글 작성자만 댓글 삭제 가능
        if (loginUser.equals(comment.getUser().getUsername()) || loginUser.equals(comment.getPost().getUser().getUsername()))
            commentService.deleteComment(commentId);

        return "redirect:/@" + username + "/" + postId;
    }
}
