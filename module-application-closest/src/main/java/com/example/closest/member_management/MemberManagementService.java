package com.example.closest.member_management;

import com.example.closest.domain.blog.Blog;
import com.example.closest.domain.blog.BlogDomain;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import com.example.closest.domain.member.MemberDomainImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberDomain memberDomain;
    private final BlogDomain blogDomain;

    @Transactional
    public void userSubscriptsBlog(String userEmail, String blogLink) {
        Member member = memberDomain.findMemberByUserEmail(userEmail);

        Blog blog = blogDomain.getOrSaveBlog(blogLink);



    }
}
