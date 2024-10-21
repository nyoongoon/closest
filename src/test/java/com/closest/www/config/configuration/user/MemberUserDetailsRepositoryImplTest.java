package com.closest.www.config.configuration.user;

import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberJpaRepository;
import com.closest.www.domain.member.MemberUserDetailsRepositoryImpl;
import com.closest.www.support.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static com.closest.www.domain.member.MemberAuthority.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;


class MemberUserDetailsRepositoryImplTest extends RepositoryTestSupport {

    @Autowired
    private MemberUserDetailsRepositoryImpl memberUserDetailsRepositoryImpl;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @DisplayName("username으로 Member의 userEmail과 일치하는 Member를 조회한 후, 인터페이스인 UserDetails 타입으로 리턴한다.")
    @Test
    void findByUsername() {
        //given
        String userEmail = "test";
        String password = "password";
        Member member = Member.builder()
                .userEmail(userEmail)
                .password(password)
                .roles(List.of(ROLE_USER))
                .build();
        memberJpaRepository.save(member);

        //when
        UserDetails found = memberUserDetailsRepositoryImpl.findByUsername(userEmail).orElseThrow();

        //then
        assertThat(found)
                .extracting(UserDetails::getUsername, UserDetails::getPassword, e->e.getAuthorities().size())
                .contains(userEmail, password, 1);
        assertThat(found.getAuthorities())
                .hasSize(1)
                .extracting(GrantedAuthority::getAuthority)
                .contains(ROLE_USER.name());
    }
}