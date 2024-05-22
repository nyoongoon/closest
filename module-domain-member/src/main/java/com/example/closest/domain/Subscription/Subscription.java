package com.example.closest.domain.Subscription;

import com.example.closest.domain.blog.Blog;
import com.example.closest.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
