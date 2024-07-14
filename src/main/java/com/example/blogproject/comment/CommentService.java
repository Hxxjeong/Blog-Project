package com.example.blogproject.comment;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.repository.PostRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    public void addComment(Long postId, String username, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("포스트를 찾을 수 없습니다."));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(content)
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);
    }
}
