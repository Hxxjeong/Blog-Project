package com.example.blogproject.blog;

import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        // 처음 생성될 때 블로그의 이름은 유저 아이디로 설정
        Blog blog = new Blog(user.getUsername(), user);
        blogRepository.save(blog);
    }

    @Transactional
    public Blog findBlog(Long id) {
        return blogRepository.findByUserId(id);
    }
}
