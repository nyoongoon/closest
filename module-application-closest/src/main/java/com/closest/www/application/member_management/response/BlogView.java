package com.closest.www.application.member_management.response;

import java.net.URL;

public record BlogView(
        String author,
        URL blogUrl,
        boolean isUpdated,
        URL lastPublishedPostUrl
) {
    public static BlogView of(
            String author,
            URL blogUrl,
            boolean isUpdated,
            URL lastPublishedPostUrl
    ) {
        return new BlogView(
                author,
                blogUrl,
                isUpdated,
                lastPublishedPostUrl
        );
    }
}
