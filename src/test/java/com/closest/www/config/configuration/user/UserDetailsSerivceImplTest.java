package com.closest.www.config.configuration.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.closest.www.domain.member.MemberAuthority.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserDetailsSerivceImplTest {

    @Autowired
    UserDetailsSerivceImpl userDetailsSerivceImpl;

    @MockBean
    UserDetailsRepository userDetailsRepository;

    @DisplayName("username으로 UserDetails를 조회한다.")
    @Test
    void loadUserByUsername() {
        // given
        String username = "testuser";
        String password = "password";
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(ROLE_USER.name()));
        UserDetails userDetails = User
                .withUsername(username)
                .password(password)
                .authorities(authorities)
                .build();
        Mockito.when(userDetailsRepository.findByUsername(username))
                .thenReturn(Optional.of(userDetails));
        //when
        UserDetails found = userDetailsSerivceImpl.loadUserByUsername(username);
        //then
        assertThat(found)
                .extracting(UserDetails::getUsername, UserDetails::getPassword, e->e.getAuthorities().size())
                .contains(username, password, 1);
        assertThat(found.getAuthorities())
                .hasSize(1)
                .extracting(GrantedAuthority::getAuthority)
                .contains(ROLE_USER.name());
    }
}