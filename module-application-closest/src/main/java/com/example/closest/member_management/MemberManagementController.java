package com.example.closest.member_management;

import com.example.closest.member_management.request.AddBlogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member-management/")
@RequiredArgsConstructor
public class MemberManagementController {
    private final MemberManagementService memberManagementService;

    @PostMapping("/blog")
    @PreAuthorize("hasRole('WRITE')")
    public void addBlog(@RequestBody AddBlogRequest addBlogRequest) {
        memberManagementService.userSubscriptsBlog(addBlogRequest.userEmail(), addBlogRequest.blogLink());
    }

}
