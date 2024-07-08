package com.closest.www.api.controller.blog.request;


import com.closest.www.config.resolver.ResolveUrl;

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
