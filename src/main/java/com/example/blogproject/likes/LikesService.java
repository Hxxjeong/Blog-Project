package com.example.blogproject.likes;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.repository.PostRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Optional<Likes> existingLike = likesRepository.findByPostAndUserUsername(post, user.getUsername());

        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
            return false;
        } else {
            Likes newLike = Likes.builder()
                    .post(post)
                    .user(user)
                    .build();
            likesRepository.save(newLike);
            return true;
        }
    }

    public boolean isPostLikedByUser(Post post, String username) {
        return likesRepository.findByPostAndUserUsername(post, username).isPresent();
    }
}
