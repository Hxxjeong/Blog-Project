package com.example.blogproject.post;

import com.example.blogproject.blog.BlogService;
import com.example.blogproject.tag.Tag;
import com.example.blogproject.tag.TagRepository;
import com.example.blogproject.uploadfile.UploadFile;
import com.example.blogproject.uploadfile.UploadFileRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BlogService blogService;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UploadFileRepository uploadFileRepository;

    // 글 작성
    @Transactional
    public Post create(Long userId, String title, String content, MultipartFile image, boolean isSecret, boolean isTemp ,List<String> tagNames) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        // 블로그 찾기
        blogService.findBlog(userId);

        List<Tag> tags = new ArrayList<>();
        for(String tagName: tagNames) {
            Tag tag = tagRepository.findByName(tagName.trim())
                    .orElseGet(() -> {
                        // 태그가 없다면 생성
                        Tag newTag = Tag.builder()
                                .name(tagName.trim())
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
                UploadFile uploadFile = saveImage(image, user);
                uploadFile.setPost(savedPost);
                uploadFileRepository.save(uploadFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return savedPost;
    }

    // 파일 저장
    private UploadFile saveImage(MultipartFile image, User user) throws IOException {
        String originName = image.getOriginalFilename();
        String storedName = System.currentTimeMillis() + "_" + originName;  // 유니크한 이름 지정

        UploadFile uploadFile = UploadFile.builder()
                .originName(originName)
                .storedName(storedName)
                .user(user)
                .build();

        File destination = new File("/c/Techit/image/" + storedName);
        image.transferTo(destination);

        return uploadFile;
    }

    // 게시글 전체 조회
    public Page<Post> getPosts(Long userId, String username, Pageable pageable) {
        return postRepository.findByUserIdAndIsSecretFalseOrUserUsernameOrderByCreateAtDesc(userId, username, pageable);
    }

    // 게시글 한 개 조회
    public Post getPost(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        return postRepository.findByUserId(user.getId());
    }
}
