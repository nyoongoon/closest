package com.closest.www.domain.member;

import com.closest.www.domain.subscription.Subscription;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Member 엔티티 - UserDetails 구현
 */
@Entity
@Table(name = "member")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique
    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false, length = 64)
    private String password;

    private String nickName;

    private List<MemberAuthority> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Subscription> subscriptions = new ArrayList<>();

    protected Member() {
    }

    private Member(Builder builder) {
        this.userEmail = builder.userEmail;
        this.password = builder.password;
        this.nickName = builder.nickName;
        this.roles = builder.roles;
    }


    public static Member create(String userEmail,
                                String password,
                                List<MemberAuthority> roles,
                                PasswordEncoder passwordEncoder) {
        return new Builder()
                .userEmail(userEmail)
                .password(passwordEncoder.encode(password)) //패스워드 암호화
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

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public List<MemberAuthority> getRoles() {
        return roles;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public static final class Builder {
        private Long id;
        private String userEmail;
        private String password;
        private String nickName;
        private List<MemberAuthority> roles;

        public Builder() {
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.nickName = name;
            return this;
        }

        public Builder roles(List<MemberAuthority> roles) {
            this.roles = roles;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
