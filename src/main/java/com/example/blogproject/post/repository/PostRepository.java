package com.example.blogproject.post.repository;

import com.example.blogproject.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 비공개 & 임시 글 필터링
    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE (p.user.id = :userId OR p.user.username = :username) " +
            "AND p.isSecret = false " +
            "AND p.isTemp = false " +
            "ORDER BY p.createAt DESC")
    Page<Post> findByUserIdAndIsSecretFalseAndIsTempFalseOrUserUsernameOrderByCreateAtDesc(
            @Param("userId") Long userId,
            @Param("username") String username,
            Pageable pageable
    );

    // 임시 글 조회
    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.user.id = :userId " +
            "AND p.isTemp = true " +
            "ORDER BY p.createAt DESC")
    Page<Post> findByUserIdAndIsTempTrue(@Param("userId") Long userId, Pageable pageable);

    // 공개글 조회 (메인 페이지용)
    Page<Post> findByIsSecretFalseAndIsTempFalseOrderByCreateAtDesc(Pageable pageable);

    // 공개글 좋아요 순으로 조회
    Page<Post> findByIsSecretFalseAndIsTempFalseOrderByLikesDesc(Pageable pageable);
}
