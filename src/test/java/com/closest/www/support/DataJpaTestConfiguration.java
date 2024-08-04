package com.closest.www.support;

import com.closest.www.domain.member.MemberUserDetailsRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataJpaTestConfiguration {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public MemberUserDetailsRepositoryImpl userDetailsRepository() {
        return new MemberUserDetailsRepositoryImpl(jpaQueryFactory());
    }
}
