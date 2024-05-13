package com.example.closest.member_management.request;

public record AddBlogRequest(
        String userEmail,
        String blogLink
) {
}
