package com.closest.www.domain.blog;

import com.closest.www.domain.Post.Post;
import com.closest.www.domain.Subscription.Subscription;
import jakarta.persistence.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private URL link;

    @Column(nullable = false, unique=true)
    private LocalDateTime lastPublishedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Post> posts = new ArrayList<>();

    protected Blog() {
    }

    private Blog(Builder builder) {
        this.link = builder.link;
        this.lastPublishedDate = builder.lastPublishedDate;
    }

    public URL getLink() {
        return link;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public static final class Builder {
        private URL link;
        private LocalDateTime lastPublishedDate;

        public Builder() {
        }

        public Builder link(URL link) {
            this.link = link;
            return this;
        }

        public Builder link(LocalDateTime lastPublishedDate) {
            this.lastPublishedDate = lastPublishedDate;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }
    }
}