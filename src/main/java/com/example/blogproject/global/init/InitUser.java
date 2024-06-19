package com.example.blogproject.global.init;

import com.example.blogproject.user.User;
import com.example.blogproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitUser implements ApplicationRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 환경변수 설정
    @Value("${adminpw}")
    private String adminPassword;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hashedPassword = passwordEncoder.encode(adminPassword);

        // 관리자 계정 생성
        User admin = User.builder()
                .username("admin")
                .password(hashedPassword)
                .name("admin")
                .email("admin@blog.com")
                .role(0)
                .build();

        userRepository.save(admin);
    }
}
