package com.example.closest.member_management;

import com.example.closest.domain.blog.Blog;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberDomain memberDomain;

    @Transactional
    public void addBlog(String userEmail, String blogLink) {
        Member member = memberDomain.findMemberByUserEmail(userEmail);
        Blog blog = Blog.builder()
                .link(blogLink)
                .build();
        blog.setMember(member);

    }
}
