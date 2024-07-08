package com.closest.www.domain.post;

import com.closest.www.domain.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteAllByBlog(Blog blog);
}
