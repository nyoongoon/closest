package com.closest.www.config.configuration;

import com.closest.www.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository {
    Optional<Member> findByUserEmail(String userEmail);
}
