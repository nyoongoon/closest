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

    public static Subscription of(Member member, Blog blog){
        Subscription subscription = new Subscription();
        subscription.setMember(member);
        subscription.setBlog(blog);
        return subscription;
    }

    public Member getMember() {
        return member;
    }

    public Blog getBlog() {
        return blog;
    }

    private void setMember(Member member) {
        if (this.member != null) {
            this.member.getSubscriptions().remove(this);
        }
        this.member = member;
        member.getSubscriptions().add(this);
    }


    private void setBlog(Blog blog) {
        if (this.blog != null) {
            this.blog.getSubscriptions().remove(this);
        }
        this.blog = blog;
        blog.getSubscriptions().add(this);
    }
}
