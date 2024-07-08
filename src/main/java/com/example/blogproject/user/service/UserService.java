package com.example.blogproject.user.service;

import com.example.blogproject.blog.BlogService;
import com.example.blogproject.role.Role;
import com.example.blogproject.role.RoleRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.TokenRepository;
import com.example.blogproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;

    // 비밀번호 해싱을 위한 객체
    private final PasswordEncoder passwordEncoder;

    private final BlogService blogService;

    // 회원 가입
    @Transactional
    public User join(User user) {
        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // 일반 유저
        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singleton(role));

        blogService.create(user.getId());   // 회원가입 시 블로그 생성

        return userRepository.save(user);
    }

    // 아이디로 찾기
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        return user;
    }

    // 이메일로 찾기
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 회원 삭제
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // 비밀번호 확인
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void saveToken(String token, String username) {
        tokenRepository.saveToken(token, username);
    }

    public Optional<String> findUsernameByToken(String token) {
        return tokenRepository.findUsernameByToken(token);
    }

    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteToken(token);
    }
}
