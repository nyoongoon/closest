package com.closest.www.domain.blog;

import org.springframework.stereotype.Service;

@Service
public interface BlogDomain {
    Blog findById(Long id);

    Blog findBlogByLink(String link);

    boolean existsByLink(String link);

    Blog findBlogByIdWithPostUsingFetchJoin(Long id);

    Blog saveByLink(String link);
}
