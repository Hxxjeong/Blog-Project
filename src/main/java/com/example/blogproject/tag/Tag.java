package com.example.blogproject.tag;

import com.example.blogproject.blog.Blog;
import com.example.blogproject.post.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
