package com.example.blogproject.uploadfile.repository;

import com.example.blogproject.uploadfile.entity.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
