package com.closest.www.support.mock;

import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberRepository;
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

    @Override
    public SecurityContext createSecurityContext(MockUser annotation) {
        Member member = new Member.Builder()
                .userEmail(annotation.email())
                .password(annotation.password())
                .build();

        var userRole = new SimpleGrantedAuthority("ROLE_USER");
        var auth = new UsernamePasswordAuthenticationToken(
                member,
                member.getPassword(),
                List.of(userRole)
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
