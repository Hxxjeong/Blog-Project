package com.example.blogproject.global.init;

import com.example.blogproject.blog.BlogService;
import com.example.blogproject.role.Role;
import com.example.blogproject.role.RoleRepository;
import com.example.blogproject.user.entity.User;
import com.example.blogproject.user.repository.UserRepository;
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
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final BlogService blogService;

    // 환경변수 설정
    @Value("${adminpw}")
    private String adminPassword;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hashedPassword = passwordEncoder.encode(adminPassword);

        // user와 admin 권한이 없으면 생성
        roleRepository.findByName("USER").orElseGet(() -> {
            Role role = new Role("USER");
            return roleRepository.save(role);
        });

        Role admin = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role("ADMIN");
            return roleRepository.save(role);
        });

        // 관리자 유저
        User adminUser = userRepository.findByUsername("admin").orElseGet(() -> {
            User userAdmin = User.builder()
                    .username("admin")
                    .password(hashedPassword)
                    .name("admin")
                    .email("admin@blog.com")
                    .build();

            userAdmin.getRoles().add(admin);    // 관리자

            return userRepository.save(userAdmin);
        });

        // 필요시 블로그 생성
        if (adminUser.getBlog() == null)
            blogService.create(adminUser.getId());
    }
}
