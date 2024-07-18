package com.example.blogproject.post.repository;

import com.example.blogproject.post.entity.Post;
import com.example.blogproject.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 전체 글 조회 (개인 블로그용)
    @Query("SELECT p FROM Post p " +
            "WHERE (p.user.id = :userId OR p.user.username = :username) " +
            "AND p.isTemp = false " +
            "AND (p.isSecret = false OR p.user.username = :currentUsername) " +
            "ORDER BY p.createAt DESC")
    Page<Post> findPostsByUserIdOrUsernameForCurrentUser(
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("currentUsername") String currentUsername,
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

    // 태그별 포스트 조회 (개인 블로그용)
    @Query("SELECT DISTINCT p FROM Post p JOIN FETCH p.tags t " +
            "WHERE (p.user.id = :userId OR p.user.username = :username) " +
            "AND p.isTemp = false " +
            "AND (p.isSecret = false OR p.user.username = :currentUsername) " +
            "AND t.name = :tagName " +
            "ORDER BY p.createAt DESC")
    Page<Post> findPostsByUserIdOrUsernameAndTagNameForCurrentUser(
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("currentUsername") String currentUsername,
            @Param("tagName") String tagName,
            Pageable pageable
    );

    // 회원 탈퇴 시 포스트 삭제하기 위함
    List<Post> findAllByUser(User user);
}
