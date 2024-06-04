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
    private URL url;

    @Column(nullable = false, unique=true)
    private LocalDateTime lastPublishedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Post> posts = new ArrayList<>();

    protected Blog() {
    }

    private Blog(Builder builder) {
        this.url = builder.url;
        this.lastPublishedDate = builder.lastPublishedDate;
    }

    public URL getUrl() {
        return url;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public static final class Builder {
        private URL url;
        private LocalDateTime lastPublishedDate;

        public Builder() {
        }

        public Builder url(URL url) {
            this.url = url;
            return this;
        }

        public Builder lastPublishedDate(LocalDateTime lastPublishedDate) {
            this.lastPublishedDate = lastPublishedDate;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }
    }
}