package com.closest.www.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Member 리포지토리
 */
@Repository
interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserEmail(String userEmail);
}
