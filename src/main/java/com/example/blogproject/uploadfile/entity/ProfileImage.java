//package com.example.blogproject.uploadfile.entity;
//
//import com.example.blogproject.user.entity.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "profile_images")
//@Getter
//@NoArgsConstructor
//@Builder
//@AllArgsConstructor
//public class ProfileImage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String originName;
//
//    private String storedName;
//
//    private boolean isDefault;  // 프로필 사진이 기본 이미지인지
//
//    @OneToOne
//    @JoinColumn(name = "user_id", unique = true)
//    private User user;
//}
