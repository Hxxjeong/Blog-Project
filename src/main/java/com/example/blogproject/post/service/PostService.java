package com.example.blogproject.post.service;

import com.example.blogproject.blog.Blog;
import com.example.blogproject.blog.BlogService;
import com.example.blogproject.post.dto.PostUpdateDto;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.post.repository.PostRepository;
import com.example.blogproject.tag.Tag;
import com.example.blogproject.tag.TagRepository;
import com.example.blogproject.uploadfile.UploadFile;
import com.example.blogproject.uploadfile.UploadFileRepository;
import com.example.blogproject.uploadfile.UploadFileService;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final BlogService blogService;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UploadFileRepository uploadFileRepository;
    private final UploadFileService uploadFileService;

    // 글 작성
    @Transactional
    public Post create(Long userId, String title, String content, MultipartFile image, boolean isSecret, boolean isTemp, List<String> tagNames) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        // 블로그 찾기
        Blog blog = blogService.findBlog(userId);

        List<Tag> tags = new ArrayList<>();
        for(String tagName: tagNames) {
            Tag tag = tagRepository.findByName(tagName.trim())
                    .orElseGet(() -> {
                        // 태그가 없다면 생성
                        Tag newTag = Tag.builder()
                                .name(tagName.trim())
                                .blog(blog)
                                .build();
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }

        Post post = Post.builder()
                .title(title)
                .content(content)
                .isSecret(isSecret)
                .isTemp(isTemp)
                .user(user)
                .tags(tags)
                .build();

        Post savedPost = postRepository.save(post);

        if(image != null && !image.isEmpty()) {
            try {
                UploadFile uploadFile = uploadFileService.saveImage(image, user);
                uploadFile.setPost(savedPost);
                uploadFileRepository.save(uploadFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return savedPost;
    }

    // 게시글 전체 조회
    public Page<Post> getPosts(Long userId, String username, Pageable pageable) {
        return postRepository.findByUserIdAndIsSecretFalseAndIsTempFalseOrUserUsernameOrderByCreateAtDesc(userId, username, pageable);
    }

    // 게시글 한 개 조회
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("포스트를 찾을 수 없습니다."));
    }

    // 글 수정
    @Transactional
    public Post update(Long postId, PostUpdateDto updateDto) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("포스트를 찾을 수 없습니다."));

        Blog blog = blogService.findBlog(post.getUser().getId());

        List<Tag> updateTags = updateDto.getTags().stream()
                .map(tagName -> tagRepository.findByNameAndBlog(tagName.trim(), blog)
                        .orElseGet(() -> new Tag(tagName, blog)))
                .collect(Collectors.toList());

        // 이미지 처리
        if (updateDto.isRemoveImage()) uploadFileService.deleteImage(post);
        else if (updateDto.getImage() != null && !updateDto.getImage().isEmpty())
            uploadFileService.updateImage(post, updateDto.getImage());

        post.update(updateDto.getTitle(), updateDto.getContent(), updateDto.isSecret(), updateDto.isTemp(), updateTags);

        return postRepository.save(post);
    }

    // 글 삭제
    @Transactional
    public void delete(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("포스트를 찾을 수 없습니다."));

        if (!post.getUser().getUsername().equals(username))
            throw new IllegalArgumentException("권한이 없습니다.");

        postRepository.delete(post);

        // 사용하지 않는 태그 삭제
        List<Tag> tags = post.getTags();
        for (Tag tag : tags) {
            if (tag.getPosts().isEmpty()) tagRepository.delete(tag);
        }
    }

    // 임시 글 전체 조회
    public Page<Post> getTemps(Long userId, Pageable pageable) {
        return postRepository.findByUserIdAndIsTempTrue(userId, pageable);
    }

    // 공개 글 조회
    public Page<Post> getPublicPosts(Pageable pageable) {
        return postRepository.findByIsSecretFalseAndIsTempFalseOrderByCreateAtDesc(pageable);
    }
}
