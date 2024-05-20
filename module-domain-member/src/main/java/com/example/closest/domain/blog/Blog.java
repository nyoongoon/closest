package com.example.closest.domain.blog;

import com.example.closest.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Blog(String link) {
        this.link = link;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getBlogs().remove(this);
        }
        this.member = member;
        member.getBlogs().add(this);
    }
}