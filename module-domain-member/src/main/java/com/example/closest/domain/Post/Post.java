package com.example.closest.domain.Post;

import com.example.closest.domain.blog.Blog;
import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String link;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    protected Post() {
    }

    private Post(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static Post of(String title, String link, Blog blog) {
        Post post = new Post(title, link);
        post.setBlog(blog);
        return post;
    }

    private void setBlog(Blog blog) {
        if (this.blog != null) {
            this.blog.getPosts().remove(this);
        }
        this.blog = blog;
        blog.getPosts().add(this);
    }
}
