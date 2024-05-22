package com.example.closest.domain.member;

import com.example.closest.domain.Subscription.Subscription;
import com.example.closest.domain.blog.Blog;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.closest.common.exception.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Member 엔티티 - UserDetails 구현
 *
 */
@Getter
@Entity
@Table(name = "blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique
    private String userEmail;

    private String password;

    private List<Authority> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Subscription> subscriptions = new ArrayList<>();

    @Builder
    public Member(String userEmail, String password, List<Authority> roles) {
        this.userEmail = userEmail;
        this.password = password;
        this.roles = roles;
    }


    public static Member of(String userEmail, String password, List<Authority> roles) {
        return Member.builder()
                .userEmail(userEmail)
                .password(password)
                .roles(roles)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(e -> new SimpleGrantedAuthority(e.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
