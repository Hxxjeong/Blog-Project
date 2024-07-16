package com.example.blogproject.post.entity;

import com.example.blogproject.comment.Comment;
import com.example.blogproject.global.entity.BaseTimeEntity;
import com.example.blogproject.post.dto.PostUpdateDto;
import com.example.blogproject.tag.Tag;
import com.example.blogproject.uploadfile.UploadFile;
import com.example.blogproject.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private UploadFile image; // Change from String to UploadFile

    private Integer likes;

    @Column(nullable = false)
    private boolean isSecret;

    @Column(nullable = false)
    private boolean isTemp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, UploadFile image, boolean isSecret, boolean isTemp, User user, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.isSecret = isSecret;
        this.isTemp = isTemp;
        this.user = user;
        if(tags != null) this.tags = tags;
    }

    public void update(PostUpdateDto updateDto, List<Tag> tags) {
        if(updateDto.getTitle() == null || updateDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if(updateDto.getContent() == null || updateDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.isSecret = updateDto.isSecret();
        this.isTemp = updateDto.isTemp();

        // 태그 업데이트
        this.tags.clear();
        this.tags.addAll(tags);
    }
}
