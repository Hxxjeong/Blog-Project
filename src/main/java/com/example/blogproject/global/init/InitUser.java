package com.example.blogproject.global.init;

import com.example.blogproject.role.Role;
import com.example.blogproject.role.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 환경변수 설정
    @Value("${adminpw}")
    private String adminPassword;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hashedPassword = passwordEncoder.encode(adminPassword);

        // InitRole보다 먼저 생성될 때 고려
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role newAdminRole = Role.builder().name("ROLE_ADMIN").build();
                    return roleRepository.save(newAdminRole);
                });

        // 관리자 계정 생성
        User admin = User.builder()
                .username("admin")
                .password(hashedPassword)
                .name("admin")
                .email("admin@blog.com")
                .build();

        admin.getRoles().add(adminRole);    // 관리자

        userRepository.save(admin);
    }
}
