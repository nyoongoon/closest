package com.closest.www.domain.blog;

import com.closest.www.api.service.blog.exception.BlogNotFoundException;
import com.closest.www.domain.post.PostRepository;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class BlogDomainImpl implements BlogDomain {
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;

    public BlogDomainImpl(BlogRepository blogRepository, PostRepository postRepository) {
        this.blogRepository = blogRepository;
        this.postRepository = postRepository;
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
    public Blog saveByUrlAndAuthor(URL url, String author) {
        Blog blog = new Blog.Builder()
                .url(url)
                .author(author)
                .build();
        return blogRepository.save(blog);
    }

    @Override
    public void clearPosts(Blog blog) {
        postRepository.deleteAllByBlog(blog);
    }
}
