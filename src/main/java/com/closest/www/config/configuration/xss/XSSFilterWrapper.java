package com.closest.www.config.configuration.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * XSS 필터 래퍼
 * - inputStream을 사용하기 위해 HttpServletRequestWrapper를 상속받음 (inputStream은 톰캣에 의하여 한 번만 읽을 수 있다)
 * <p>
 * - text/plain 필터링
 * - application/x-www-urlencoded 필터링
 * - application/json 필터링 -> json은 따로 messageConverter로 (\")변환이슈
 * - multipart/form-data 필터링
 */

public class XSSFilterWrapper extends HttpServletRequestWrapper {
    // 이스케이프를 위한 Translator
    private final CharSequenceTranslator translator;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XSSFilterWrapper(HttpServletRequest request, CharSequenceTranslator translator) {
        super(request);
        this.translator = translator;
    }

    private byte[] replaceXSS(byte[] data) {
        String strData = new String(data);
        return replaceXSS(strData).getBytes();
    }

    private String replaceXSS(String value) {
        if (value != null) {
            value = this.translator.translate(value);
        }
        return value;
    }

    @Override
    public String getQueryString() {
        return replaceXSS(super.getQueryString());
    }


    @Override
    public String getParameter(String name) {
        return replaceXSS(super.getParameter(name));
    }

    /**
     * GET인 경우만 사용 (요청이 Http Body 값인 경우 사용안하기 위함)
     * - GET이 아니여도 application/x-www-form-urlencoded 등의 경우 해당 메서드가 사용되는데
     * - -> 이스케이프 처리 시 HttpMessageConverter 까지 타게 되므로 이스케이핑이 두번 되는 이슈가 있다.
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> params = super.getParameterMap();
        if (!getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
            return params;
        }
        if (params != null) {
            params.forEach((key, value) -> {
                for (int i = 0; i < value.length; i++) {
                    value[i] = replaceXSS(value[i]);
                }
            });
        }
        return params;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] params = super.getParameterValues(name);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                params[i] = replaceXSS(params[i]);
            }
        }
        return params;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
    }
}