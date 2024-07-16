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
    private UploadFile image;

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

    // 글 수정
    public void update(String title, String content, boolean isSecret, boolean isTemp, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
        this.isTemp = isTemp;
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public void setImage(UploadFile image) {
        if (this.image != null) {
            this.image.setPost(null);
        }
        this.image = image;
        if (image != null) {
            image.setPost(this);
        }
    }

    public void removeImage() {
        if (this.image != null) {
            this.image.setPost(null);
            this.image = null;
        }
    }
}
