package com.closest.www.api.service.blog.request;

import java.net.URL;

public record AddBlogServiceRequest(
        String userEmail,
        URL url
) {
}
