package com.example.blogproject.likes;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostAndUserUsername(Post post, String username);
    void deleteAllByUser(User user);
}
