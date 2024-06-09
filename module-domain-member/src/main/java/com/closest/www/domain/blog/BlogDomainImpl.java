package com.closest.www.domain.blog;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;

@Service
public class BlogDomainImpl implements BlogDomain {
    private final BlogRepository blogRepository;

    public BlogDomainImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public Blog findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(()-> new BlogNotFoundException());
    }

    @Override
    public boolean existsByUrl(URL url) {
        return blogRepository.existsByUrl(url);
    }

    @Override
    public Blog findBlogByUrl(URL url) {
        return blogRepository.findByUrl(url)
                .orElseThrow(() -> new BlogNotFoundException());
    }

    @Override
    public Blog findBlogByIdWithPostUsingFetchJoin(Long id) {
        return blogRepository.findBlogByIdWithPostUsingFetchJoin(id)
                .orElseThrow(() -> new BlogNotFoundException());
    }

    @Override
    public Blog saveByUrl(URL url) {
        Blog blog = new Blog.Builder()
                .url(url)
                .build();
        return blogRepository.save(blog);
    }
}
