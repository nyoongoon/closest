package com.closest.www.api.controller.blog.response;

import java.net.URL;

public record BlogResponse(
        String author,
        URL blogUrl,
        boolean isUpdated,
        URL lastPublishedPostUrl
) {
    public static BlogResponse of(
            String author,
            URL blogUrl,
            boolean isUpdated,
            URL lastPublishedPostUrl
    ) {
        return new BlogResponse(
                author,
                blogUrl,
                isUpdated,
                lastPublishedPostUrl
        );
    }
}
