package com.closest.www.config.configuration.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.translate.CharSequenceTranslator;

import java.io.IOException;

/**
 * MultipartFilter 다음에 실행될 수 있도록
 */
public class XssFilter implements Filter {

    public XssFilter(CharSequenceTranslator translator) {
        this.translator = translator;
    }

    private final CharSequenceTranslator translator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServleRequest = (HttpServletRequest) request;
        HttpServletRequestWrapper requestWrapper = new XSSFilterWrapper(httpServleRequest, translator);
        chain.doFilter(requestWrapper, response);
    }
}
