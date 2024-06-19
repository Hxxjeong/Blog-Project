package com.example.blogproject.user;

import com.example.blogproject.blog.Blog;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;    // 사용자 로그인 아이디

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;    // 사용자가 설정한 이름

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int role; // user: 1, admin: 0

    @CreatedDate
    @Column(name = "registration_date") // 가입 날짜
    private LocalDateTime date;

//    @ManyToMany
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();

    // 외래키를 가지고 있는 테이블이 먼저 생성되는 것 방지 (Eager Loading)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Blog> blogs = new HashSet<>();

    @Builder
    public User(String username, String password, String name, String email, int role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
