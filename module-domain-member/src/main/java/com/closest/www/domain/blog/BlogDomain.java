package com.closest.www.domain.blog;

import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public interface BlogDomain {
    Blog findById(Long id);

    Blog findBlogByUrl(URL url);

    boolean existsByUrl(URL url);

    Blog findBlogByIdWithPostUsingFetchJoin(Long id);

    Blog saveByUrlAndAuthor(URL url, String author);

    void clearPosts(Blog blog);
}
