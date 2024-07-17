package com.closest.www.domain.feed;

import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.Optional;

@Repository
public interface FeedRepository {
    Optional<Feed> findByUrl(URL url);
}
