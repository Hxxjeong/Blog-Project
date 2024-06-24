package com.example.blogproject.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long id);
    Page<Post> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);   // 페이징 처리
}
