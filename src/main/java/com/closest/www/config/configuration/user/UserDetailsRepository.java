package com.closest.www.config.configuration.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository {
    Optional<UserDetails> findByUsername(String username);
}
