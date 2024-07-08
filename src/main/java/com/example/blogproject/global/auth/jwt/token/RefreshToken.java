//package com.example.blogproject.global.auth.jwt.token;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "refresh_token")
//@Getter
//@Setter
//public class RefreshToken {     // 리프레시 토큰 관리
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_id")
//    private Long userId;
//
//    private String value;
//
//    @Builder
//    public RefreshToken(Long userId, String value) {
//        this.userId = userId;
//        this.value = value;
//    }
//}
