package com.closest.www.api.controller.blog;

import com.closest.www.api.ApiResponse;
import com.closest.www.api.controller.blog.request.AddBlogRequest;
import com.closest.www.api.service.blog.BlogService;
import com.closest.www.config.filter.JwtAuthenticationFilter;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
    public ApiResponse<Void> addBlog(@RequestBody @Valid AddBlogRequest addBlogRequest) {
        blogService.memberSubscriptsBlog(addBlogRequest.toServiceRequest());
        return ApiResponse.ok();
    }
}
