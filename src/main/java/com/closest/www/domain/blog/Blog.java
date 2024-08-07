package com.closest.www.domain.blog;

import com.closest.www.domain.post.Post;
import com.closest.www.domain.subscription.Subscription;
import jakarta.persistence.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private Date lastPublishedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Post> posts = new ArrayList<>();

    protected Blog() {
    }

    public static Blog.Builder builder(){
        return new Blog.Builder();
    }

    private Blog(Builder builder) {
        this.url = builder.url;
        this.author = builder.author;
        this.lastPublishedDate = builder.build().lastPublishedDate;
    }

    public static Blog create(
            URL url,
            String author,
            Date lastPublishedDate
    ){
        return Blog.builder()
                .url(url)
                .author(author)
                .lastPublishedDate(lastPublishedDate)
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

    public Date getLastPublishedDate() {
        return lastPublishedDate;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void updateLastPublishedDate(Date lastPublishedDate) {
        this.lastPublishedDate = lastPublishedDate;
    }

    //todo 업데이트 여부 방식 바뀌어야함 (기존 날짜 타입이 LocalDatetime에서 Date로 변경됨- rss에서 Date로 주는 거 같음..)
    public boolean isUpdated(Date lastPublishedDate) {
        return this.lastPublishedDate != lastPublishedDate;
    }

    public static final class Builder {
        private URL url;
        private String author;
        private Date lastPublishedDate; // todo 사용하지 않는 이유 찾아보기

        private Builder() {
        }

        public Builder url(URL url) {
            this.url = url;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder lastPublishedDate(Date lastPublishedDate) {
            this.lastPublishedDate = lastPublishedDate;
            return this;
        }

        public Blog build() {
            return new Blog(this);
        }
    }
}