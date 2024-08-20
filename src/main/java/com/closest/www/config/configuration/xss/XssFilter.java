package com.closest.www.config.configuration.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class XssFilter implements Filter {

    private final CharSequenceTranslator translator;

    public XssFilter(CharSequenceTranslator translator) {
        this.translator = translator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequestWrapper requestWrapper = new XSSFilterWrapper((HttpServletRequest) request, translator);
        chain.doFilter(requestWrapper, response);
    }
}
