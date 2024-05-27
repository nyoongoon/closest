package com.example.closest.member_management;

import com.example.closest.domain.blog.BlogDomain;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberManagementServiceTest {
    @Autowired
    private MemberDomain memberDomain;
    @Autowired
    private MemberManagementService memberManagementService;
    @Qualifier("blogDomain")
    @Autowired
    private BlogDomain blogDomain;

    @Test
    @DisplayName("블로그 엔티티 등록하고 멤버 연관관계 맺는다")
    @Transactional
    void test1() {
        // given
        String userEmail = "abc@naver.com";
        Member member = new Member.Builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberDomain.regist(member);
        String link = "https://goalinnext.tistory.com";
        // when
        memberManagementService.userSubscriptsBlog(userEmail, link);
        // then
        assertThat(member.getSubscriptions().get(0).getBlog().getLink()).isEqualTo(link);
    }
}