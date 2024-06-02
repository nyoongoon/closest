package com.example.closest.domain.blog;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BlogDomain {
    Optional<Blog> findById(Long id);

    Optional<Blog> findBlogByLink(String link);

    Optional<Blog> findBlogByIdWithPostUsingFetchJoin(Long id);

    Blog saveBlogByLink(String link);
}
