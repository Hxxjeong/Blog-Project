package com.example.blogproject.user.service;

import com.example.blogproject.blog.Blog;
import com.example.blogproject.blog.BlogRepository;
import com.example.blogproject.comment.CommentRepository;
import com.example.blogproject.likes.LikesRepository;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.repository.PostRepository;
import com.example.blogproject.role.Role;
import com.example.blogproject.role.RoleRepository;
import com.example.blogproject.tag.entity.Tag;
import com.example.blogproject.tag.repository.TagRepository;
import com.example.blogproject.uploadfile.repository.ThumbnailRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.TokenRepository;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final TagRepository tagRepository;
    private final ThumbnailRepository thumbnailRepository;

    // 비밀번호 해싱을 위한 객체
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public User join(User user) {
        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 일반 유저
        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(role));

        return userRepository.save(user);
    }

    // 아이디로 찾기
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        return user;
    }

    // 이메일로 찾기
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        // 좋아요 삭제
        likesRepository.deleteAllByUser(user);

        // 댓글 삭제
        commentRepository.deleteAllByUser(user);

        // 유저가 작성한 포스트 삭제
        List<Post> userPosts = postRepository.findAllByUser(user);
        for (Post post : userPosts) {
            // 태그 삭제
            post.getTags().clear();
            // 썸네일 삭제
            if (post.getImage() != null) {
                thumbnailRepository.delete(post.getImage());
            }
        }
        postRepository.deleteAll(userPosts);

        // 유저와 관련된 태그 삭제
        Blog userBlog = blogRepository.findByUserId(user.getId());
        if (userBlog != null) {
            List<Tag> blogTags = tagRepository.findAllByBlog(userBlog);
            tagRepository.deleteAll(blogTags);
            blogRepository.delete(userBlog);
        }

        // 유저와 역할 연결 삭제
        user.getRoles().clear();

        // 유저 삭제
        userRepository.delete(user);
    }

    // 비밀번호 확인
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void saveToken(String token, String username) {
        tokenRepository.saveToken(token, username);
    }

    public Optional<String> findUsernameByToken(String token) {
        return tokenRepository.findUsernameByToken(token);
    }

    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteToken(token);
    }
}
