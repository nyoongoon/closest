package com.closest.www.application.member_management;

import com.closest.www.application.member_management.request.AddBlogRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member-management/")
public class MemberManagementController {
    private final MemberManagementService memberManagementService;

    public MemberManagementController(MemberManagementService memberManagementService) {
        this.memberManagementService = memberManagementService;
    }

    @PostMapping("/blog")
    @PreAuthorize("hasRole('WRITE')")
    public void addBlog(@RequestBody AddBlogRequest addBlogRequest) {
        memberManagementService.userSubscriptsBlog(addBlogRequest.userEmail(), addBlogRequest.url());
    }
}
