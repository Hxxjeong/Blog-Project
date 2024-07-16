package com.example.blogproject.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class PostUpdateDto {
    private String title;
    private String content;
    private boolean isSecret;
    private boolean isTemp;
    private List<String> tags;  // 태그 목록
    private MultipartFile image;
    private boolean removeImage;
}
