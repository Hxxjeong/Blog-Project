package com.example.blogproject.post;

import com.example.blogproject.global.entity.BaseTimeEntity;
import com.example.blogproject.tag.Tag;
import com.example.blogproject.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(nullable = false)

    private String title;
    @Column(nullable = false)

    private String content;

    private String image;

    private Integer likes;

    @Column(nullable = false)
    private boolean isSecret;

    @Column(nullable = false)
    private boolean isTemp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Post(String title, String content, String image, boolean isSecret, boolean isTemp, User user, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.isSecret = isSecret;
        this.isTemp = isTemp;
        this.user = user;
        if(tags != null) this.tags = tags;
    }
}
