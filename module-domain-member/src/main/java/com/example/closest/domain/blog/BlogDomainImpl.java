package com.example.closest.domain.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogDomainImpl implements BlogDomain{

    @Override
    public Blog getOrSaveBlog(String blogLink) {
        return null;
    }
}
