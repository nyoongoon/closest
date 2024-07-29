package com.closest.www.config.configuration;

import com.closest.www.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.closest.www.domain.member.QMember.member;

@Repository
public class UserDetailsRepositoryImpl implements UserDetailsRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public UserDetailsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Member> findByUserEmail(String userEmail) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(member.userEmail.eq(userEmail))
                .fetchOne()
        );
    }
}
