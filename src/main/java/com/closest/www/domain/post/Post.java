package com.closest.www.domain.post;

import com.closest.www.domain.blog.Blog;
import jakarta.persistence.*;

import java.net.URL;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private URL url;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    protected Post() {
    }

    private Post(String title, URL url) {
        this.title = title;
        this.url = url;
    }

    public static Post of(String title, URL url, Blog blog) {
        Post post = new Post(title, url);
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
