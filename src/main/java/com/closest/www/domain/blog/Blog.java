package com.closest.www.domain.blog;

import com.closest.www.domain.post.Post;
import com.closest.www.domain.subscription.Subscription;
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

    @Column(nullable = false, unique = true)
    private URL url;

    @Column(nullable = false)
    private String author;

    @Column
    private LocalDateTime lastPublishedLocalDateTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Post> posts = new ArrayList<>();

    protected Blog() {
    }

    private Blog(Builder builder) {
        this.url = builder.url;
        this.author = builder.author;
        this.lastPublishedLocalDateTime = builder.lastPublishedLocalDateTime;
    }

    public static Blog create(
            URL url,
            String author,
            LocalDateTime lastPublishedLocalDateTime
    ){
        return new Blog.Builder()
                .url(url)
                .author(author)
                .lastPublishedLocalDateTime(lastPublishedLocalDateTime)
                .build();
    }

    public Long getId() {
        return id;
    }

    public URL getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getLastPublishedLocalDateTime() {
        return lastPublishedLocalDateTime;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void updateLastPublishedDate(LocalDateTime lastPublishedDate) {
        this.lastPublishedLocalDateTime = lastPublishedDate;
    }

    public boolean isUpdated(LocalDateTime lastPublishedDate) {
        return this.lastPublishedLocalDateTime != lastPublishedDate;
    }

    public static final class Builder {
        private URL url;
        private String author;
        private LocalDateTime lastPublishedLocalDateTime;

        public Builder() {
        }

        public Builder url(URL url) {
            this.url = url;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder lastPublishedLocalDateTime(LocalDateTime lastPublishedDate) {
            this.lastPublishedLocalDateTime = lastPublishedDate;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }
    }
}