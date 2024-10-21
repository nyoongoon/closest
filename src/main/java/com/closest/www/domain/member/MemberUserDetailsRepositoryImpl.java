package com.closest.www.domain.member;

import com.closest.www.config.configuration.user.UserDetailsRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.closest.www.domain.member.QMember.member;

@Repository
public class MemberUserDetailsRepositoryImpl implements UserDetailsRepository {
    private final JPAQueryFactory jpaQueryFactory;


    public MemberUserDetailsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<UserDetails> findByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(member.userEmail.eq(username)) // Member의 userEmail을 username로 조회
                .fetchOne()
        );
    }
}
