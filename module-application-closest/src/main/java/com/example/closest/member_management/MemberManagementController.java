package com.example.closest.member_management;

import com.example.closest.member_management.request.AddBlogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberManagementController {
    private final MemberManagementService memberManagementService;

    @PostMapping("/posts")
    public void addBlog(AddBlogRequest addBlogRequest){
        memberManagementService.addBlog(addBlogRequest.userEmail(), addBlogRequest.blogLink());
    }

}
