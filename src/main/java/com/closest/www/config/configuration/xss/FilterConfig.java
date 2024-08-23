package com.closest.www.config.configuration.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
public class FilterConfig {
    private final ObjectMapper objectMapper;
    private final CharSequenceTranslator translator;

    public FilterConfig(ObjectMapper objectMapper, CharSequenceTranslator translator) {
        this.objectMapper = objectMapper;
        this.translator = translator;
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean = new FilterRegistrationBean<>(new XssFilter(translator));
        xssFilterFilterRegistrationBean.setOrder(2);
        return xssFilterFilterRegistrationBean;
    }


    /**
     * application/x-www-form-urlencoded
     * @return
     */
    @Bean
    public FormHttpMessageConverter formUrlEncodedEscapeConverter() {
        return new FormHttpMessageConverter() {
            @Override
            public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz,
                                                      HttpInputMessage inputMessage) throws IOException {
                MultiValueMap<String, String> body = super.read(clazz, inputMessage);
                return escapeMap(body);
            }

            private MultiValueMap<String, String> escapeMap(MultiValueMap<String, String> map) {
                MultiValueMap<String, String> escapedMap = new LinkedMultiValueMap<>();
                map.forEach((key, valueList) ->
                        escapedMap.put(translator.translate(key),
                                valueList.stream()
                                        .map(translator::translate)
                                        .collect(Collectors.toList()))
                );
                return escapedMap;
            }
        };
    }


    /**
     * text/plain & multipart/form-data 문자열 파라미터
     * @return
     */
    @Bean
    public StringHttpMessageConverter textEscapeConverter() {
        return new StringHttpMessageConverter() {
            @Override
            protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage)
                    throws IOException {
                String body = super.readInternal(clazz, inputMessage);
                return translator.translate(body);
            }
        };
    }

    /**
     * application/json & multipart/form-data 객체 파라미터
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        objectMapper.getFactory().setCharacterEscapes(new XssCharacterEscapes(translator));

        return new MappingJackson2HttpMessageConverter(objectMapper) {
            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                // 응답인 경우에 요청 태우지 않는다.
                return false;
            }
        };
    }
}