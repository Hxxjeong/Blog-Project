package com.example.blogproject.uploadfile.entity;

import com.example.blogproject.global.entity.BaseTimeEntity;
import com.example.blogproject.post.entity.Post;
import com.example.blogproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "thumbnails")
@Getter
@Setter
@NoArgsConstructor
public class Thumbnail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "stored_name", unique = true, nullable = false)
    private String storedName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", unique = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Thumbnail(String originName, String storedName, User user) {
        this.originName = originName;
        this.storedName = storedName;
        this.user = user;
    }
}
