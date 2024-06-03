package com.closest.www.application.member_management.request;


public record AddBlogRequest(
        String userEmail,
        String blogLink
) {
    public static AddBlogRequest of(
            String userEmail,
            String blogLink
    ) {
        return new AddBlogRequest(
                userEmail,
                blogLink
        );
    }
}
