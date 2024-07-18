package com.example.blogproject.comment;

import com.example.blogproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteAllByUser(User user);
}
