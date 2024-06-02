package com.example.closest.domain.blog;

import com.example.closest.domain.Post.Post;
import com.example.closest.domain.Subscription.Subscription;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String link;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Post> posts = new ArrayList<>();

    protected Blog() {
    }

    private Blog(Builder builder) {
        this.link = builder.link;
    }

    public String getLink() {
        return link;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public static final class Builder {
        private String link;

        public Builder() {
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }
    }
}