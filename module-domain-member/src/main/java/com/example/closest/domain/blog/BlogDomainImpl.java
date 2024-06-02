package com.example.closest.domain.blog;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlogDomainImpl implements BlogDomain {
    private final BlogRepository blogRepository;

    public BlogDomainImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Optional<Blog> findBlogByLink(String link) {
        return blogRepository.findByLink(link);
    }

    @Override
    public Optional<Blog> findBlogByIdWithPostUsingFetchJoin(Long id) {
        return Optional.empty()
    }

    @Override
    public Blog saveBlogByLink(String link) {
        Blog blog = new Blog.Builder()
                .link(link)
                .build();
        return blogRepository.save(blog);
    }
}
