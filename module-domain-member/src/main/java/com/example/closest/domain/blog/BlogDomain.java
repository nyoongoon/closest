package com.example.closest.domain.blog;

import org.springframework.stereotype.Service;

@Service
public interface BlogDomain {

    Blog getOrSaveBlog(String blogLink);
}
