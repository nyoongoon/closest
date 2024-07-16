package com.closest.www.domain.member;

import com.closest.www.config.configuration.UserDetailsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member 리포지토리
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, UserDetailsRepository {
    Optional<Member> findByUserEmail(String userEmail);

    boolean existsByUserEmail(String userEmail);
}
