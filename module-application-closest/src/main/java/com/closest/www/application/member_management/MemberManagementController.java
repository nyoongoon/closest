package com.closest.www.application.member_management;

import com.closest.www.application.member_management.request.AddBlogRequest;
import com.closest.www.application.rss.FailToReadFeedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/member-management/")
public class MemberManagementController {
    private final MemberManagementService memberManagementService;

    public MemberManagementController(MemberManagementService memberManagementService) {
        this.memberManagementService = memberManagementService;
    }

    @PostMapping("/blog")
    @PreAuthorize("hasRole('WRITE')")
    public void addBlog(@RequestBody AddBlogRequest addBlogRequest) throws FailToReadFeedException {
        memberManagementService.memberSubscriptsBlog(addBlogRequest.userEmail(), addBlogRequest.url());
    }
}
