package com.closest.www.application.member_management.request;


import com.closest.www.application.member_management.config.ResolveUrl;

import java.net.URL;

public record AddBlogRequest(
        String userEmail,
        @ResolveUrl URL url
) {
    public static AddBlogRequest of(
            String userEmail,
            URL url
    ) {
        return new AddBlogRequest(
                userEmail,
                url
        );
    }
}
