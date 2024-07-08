package com.example.blogproject.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);   // 페이징 처리

    // 비공개글 필터링
    Page<Post> findByUserIdAndIsSecretFalseOrUserUsernameOrderByCreateAtDesc(Long userId, String username, Pageable pageable);

    Post findByUserId(Long userId);
}
