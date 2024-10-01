package com.closest.www.config.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URL;

import static com.closest.www.api.controller.exception.ControllerExceptionMessageConstants.URL_IS_REQUIRED;

/**
 * @see ResolveUrl
 */
public class UrlResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getAnnotatedElement().equals(ResolveUrl.class) && parameter.getParameterType().equals(URL.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String url = webRequest.getParameter("url");
        if(StringUtils.isBlank(url)){
            throw new IllegalArgumentException(URL_IS_REQUIRED);
        }
        return new URL(url);
    }
}
