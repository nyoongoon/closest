package com.closest.www.api.controller.blog;

import com.closest.www.api.controller.blog.request.AddBlogRequest;
import com.closest.www.api.service.blog.BlogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('WRITE')")
    public void addBlog(@RequestBody AddBlogRequest addBlogRequest) {
        blogService.memberSubscriptsBlog(addBlogRequest.userEmail(), addBlogRequest.url());
    }
}
