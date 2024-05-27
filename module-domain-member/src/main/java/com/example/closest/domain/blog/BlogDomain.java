package com.example.closest.domain.blog;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BlogDomain {

    Optional<Blog> findBlogByLink(String link);

    Blog saveBlogByLink(String link);
}
