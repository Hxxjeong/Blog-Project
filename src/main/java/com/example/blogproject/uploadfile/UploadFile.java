package com.example.blogproject.uploadfile;

import com.example.blogproject.global.entity.BaseTimeEntity;
import com.example.blogproject.post.Post;
import com.example.blogproject.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "upload_files")
@Getter
@Setter
@NoArgsConstructor
public class UploadFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_name", nullable = false)
    private String originName;
    @Column(name = "stored_name", unique = true, nullable = false)
    private String storedName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UploadFile(String originName, String storedName, User user) {
        this.originName = originName;
        this.storedName = storedName;
        this.user = user;
    }
}
