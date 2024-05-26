package com.example.closest.domain.Subscription;

import com.example.closest.domain.blog.Blog;
import com.example.closest.domain.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    protected Subscription(){
    }

    private Subscription(Builder builder) {
        setMember(builder.member);
        setBlog(builder.blog);
    }

    public Member getMember() {
        return member;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getSubscriptions().remove(this);
        }
        this.member = member;
        member.getSubscriptions().add(this);
    }


    public void setBlog(Blog blog) {
        if (this.member != null) {
            this.member.getSubscriptions().remove(this);
        }
        this.member = member;
        member.getSubscriptions().add(this);
    }


    public static final class Builder {
        private Member member;
        private Blog blog;

        public Builder() {
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder blog(Blog blog) {
            this.blog = blog;
            return this;
        }

        public Subscription build() {
            return new Subscription(this);
        }
    }
}
