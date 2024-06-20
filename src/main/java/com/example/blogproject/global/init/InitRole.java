package com.example.blogproject.global.init;

import com.example.blogproject.role.Role;
import com.example.blogproject.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitRole implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Role user = Role.builder()
                .name("ROLE_USER")
                .build();

        Role admin = Role.builder()
                .name("ROLE_ADMIN")
                .build();

        roleRepository.save(user);
        roleRepository.save(admin);
    }
}
