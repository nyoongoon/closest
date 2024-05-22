package com.example.closest.config;

import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

/**
 * 테스트용 스프링 시큐리티 컨텍스트에 Authentication 추가
 */
public class MockSecurityContext implements WithSecurityContextFactory<MockUser> {
    private final MemberDomain memberDomain;

    public MockSecurityContext(MemberDomain memberDomain) {
        this.memberDomain = memberDomain;
    }

    @Override
    public SecurityContext createSecurityContext(MockUser annotation) {
        Member member = Member.builder()
                .userEmail(annotation.email())
                .password(annotation.password())
                .build();
        memberDomain.regist(member);


        var role = new SimpleGrantedAuthority("ROLE_WRITE");
        var auth = new UsernamePasswordAuthenticationToken(
                member,
                member.getPassword(),
                List.of(role)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
