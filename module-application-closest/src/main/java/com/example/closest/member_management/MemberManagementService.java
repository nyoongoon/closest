package com.example.closest.member_management;

import com.example.closest.domain.Subscription.Subscription;
import com.example.closest.domain.blog.Blog;
import com.example.closest.domain.blog.BlogDomain;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberManagementService {
    private final MemberDomain memberDomain;
    private final BlogDomain blogDomain;

    public MemberManagementService(MemberDomain memberDomain, BlogDomain blogDomain) {
        this.memberDomain = memberDomain;
        this.blogDomain = blogDomain;
    }

    @Transactional
    public void userSubscriptsBlog(String userEmail, String link) {
        Member member = memberDomain.findMemberByUserEmail(userEmail);
        Blog blog = blogDomain.findBlogByLink(link)
                .orElseGet(() -> blogDomain.saveBlogByLink(link));

        new Subscription.Builder() //persistence cascade
                .member(member)
                .blog(blog)
                .build();
    }
}
