package com.closest.www.domain.blog;

import org.springframework.stereotype.Service;

import java.net.URL;

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
    public boolean existsByLink(URL link) {
        return blogRepository.existsByLink(link);
    }

    @Override
    public Blog findBlogByLink(URL link) {
        return blogRepository.findByLink(link)
                .orElseThrow(() -> new BlogNotFoundException());
    }

    @Override
    public Blog findBlogByIdWithPostUsingFetchJoin(Long id) {
        return blogRepository.findBlogByIdWithPostUsingFetchJoin(id)
                .orElseThrow(() -> new BlogNotFoundException());
    }

    @Override
    public Blog saveByLink(String link) {


        Blog blog = new Blog.Builder()
                .link(link)
                .build();
        return blogRepository.save(blog);
    }
}
