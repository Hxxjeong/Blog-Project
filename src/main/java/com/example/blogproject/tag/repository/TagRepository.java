package com.example.blogproject.tag.repository;

import com.example.blogproject.blog.Blog;
import com.example.blogproject.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndBlog(String name, Blog blog);

    // 블로그에서 사용한 태그 개수 확인
    @Query("SELECT t, COUNT(p) FROM Tag t LEFT JOIN t.posts p " +
            "WHERE t.blog.id = :blogId AND (p IS NULL OR p.isTemp = false) " +
            "AND (p.isSecret = false OR p.user.username = :currentUsername) " +
            "GROUP BY t.id")
    List<Object[]> findTagsWithPostCountByBlogId(@Param("blogId") Long blogId, @Param("currentUsername") String currentUsername);

    List<Tag> findAllByBlog(Blog blog);
}
