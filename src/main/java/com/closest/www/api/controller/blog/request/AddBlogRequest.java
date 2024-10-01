package com.closest.www.api.controller.blog.request;


import com.closest.www.api.service.blog.request.AddBlogServiceRequest;
import com.closest.www.config.resolver.ResolveUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.URL;

import static com.closest.www.api.controller.exception.ControllerExceptionMessageConstants.EMAIL_IS_REQUIRED;
import static com.closest.www.api.controller.exception.ControllerExceptionMessageConstants.URL_IS_REQUIRED;

public record AddBlogRequest(
        @NotBlank(message = EMAIL_IS_REQUIRED)
//        @Email(message = NOT_VALID_EMAIL_FORM)
        String userEmail,
        @NotNull(message = URL_IS_REQUIRED) @ResolveUrl URL url
) {
    public AddBlogServiceRequest toServiceRequest() {
        return new AddBlogServiceRequest(
                userEmail,
                url
        );
    }
}
